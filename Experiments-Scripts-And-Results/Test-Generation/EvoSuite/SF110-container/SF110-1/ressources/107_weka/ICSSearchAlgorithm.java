package weka.classifiers.bayes.net.search.ci;

import weka.classifiers.bayes.*;
import weka.classifiers.bayes.net.*;
import java.util.*;
import weka.classifiers.bayes.net.search.*;
import java.io.*;
import weka.core.*;

public class ICSSearchAlgorithm extends CISearchAlgorithm
{
    static final long serialVersionUID = -2510985917284798576L;
    private int m_nMaxCardinality;
    
    public ICSSearchAlgorithm() {
        this.m_nMaxCardinality = 2;
    }
    
    String name(final int iAttribute) {
        return this.m_instances.attribute(iAttribute).name();
    }
    
    int maxn() {
        return this.m_instances.numAttributes();
    }
    
    public void setMaxCardinality(final int nMaxCardinality) {
        this.m_nMaxCardinality = nMaxCardinality;
    }
    
    public int getMaxCardinality() {
        return this.m_nMaxCardinality;
    }
    
    @Override
    protected void search(final BayesNet bayesNet, final Instances instances) throws Exception {
        this.m_BayesNet = bayesNet;
        this.m_instances = instances;
        final boolean[][] edges = new boolean[this.maxn() + 1][];
        final boolean[][] arrows = new boolean[this.maxn() + 1][];
        final SeparationSet[][] sepsets = new SeparationSet[this.maxn() + 1][];
        for (int iNode = 0; iNode < this.maxn() + 1; ++iNode) {
            edges[iNode] = new boolean[this.maxn()];
            arrows[iNode] = new boolean[this.maxn()];
            sepsets[iNode] = new SeparationSet[this.maxn()];
        }
        this.calcDependencyGraph(edges, sepsets);
        this.calcVeeNodes(edges, arrows, sepsets);
        this.calcArcDirections(edges, arrows);
        for (int iNode = 0; iNode < this.maxn(); ++iNode) {
            final ParentSet oParentSet = this.m_BayesNet.getParentSet(iNode);
            while (oParentSet.getNrOfParents() > 0) {
                oParentSet.deleteLastParent(this.m_instances);
            }
            for (int iParent = 0; iParent < this.maxn(); ++iParent) {
                if (arrows[iParent][iNode]) {
                    oParentSet.addParent(iParent, this.m_instances);
                }
            }
        }
    }
    
    void calcDependencyGraph(final boolean[][] edges, final SeparationSet[][] sepsets) {
        for (int iNode1 = 0; iNode1 < this.maxn(); ++iNode1) {
            for (int iNode2 = 0; iNode2 < this.maxn(); ++iNode2) {
                edges[iNode1][iNode2] = true;
            }
        }
        for (int iNode1 = 0; iNode1 < this.maxn(); ++iNode1) {
            edges[iNode1][iNode1] = false;
        }
        for (int iCardinality = 0; iCardinality <= this.getMaxCardinality(); ++iCardinality) {
            for (int iNode3 = 0; iNode3 <= this.maxn() - 2; ++iNode3) {
                for (int iNode4 = iNode3 + 1; iNode4 < this.maxn(); ++iNode4) {
                    if (edges[iNode3][iNode4]) {
                        final SeparationSet oSepSet = this.existsSepSet(iNode3, iNode4, iCardinality, edges);
                        if (oSepSet != null) {
                            edges[iNode3][iNode4] = false;
                            edges[iNode4][iNode3] = false;
                            sepsets[iNode3][iNode4] = oSepSet;
                            sepsets[iNode4][iNode3] = oSepSet;
                            System.err.print("I(" + this.name(iNode3) + ", {");
                            for (int iNode5 = 0; iNode5 < iCardinality; ++iNode5) {
                                System.err.print(this.name(oSepSet.m_set[iNode5]) + " ");
                            }
                            System.err.print("} ," + this.name(iNode4) + ")\n");
                        }
                    }
                }
            }
            System.err.print(iCardinality + " ");
            for (int iNode3 = 0; iNode3 < this.maxn(); ++iNode3) {
                System.err.print(this.name(iNode3) + " ");
            }
            System.err.print('\n');
            for (int iNode3 = 0; iNode3 < this.maxn(); ++iNode3) {
                for (int iNode4 = 0; iNode4 < this.maxn(); ++iNode4) {
                    if (edges[iNode3][iNode4]) {
                        System.err.print("X ");
                    }
                    else {
                        System.err.print(". ");
                    }
                }
                System.err.print(this.name(iNode3) + " ");
                System.err.print('\n');
            }
        }
    }
    
    SeparationSet existsSepSet(final int iNode1, final int iNode2, final int nCardinality, final boolean[][] edges) {
        final SeparationSet Z = new SeparationSet();
        Z.m_set[nCardinality] = -1;
        if (nCardinality > 0) {
            Z.m_set[0] = this.next(-1, iNode1, iNode2, edges);
            for (int iNode3 = 1; iNode3 < nCardinality; ++iNode3) {
                Z.m_set[iNode3] = this.next(Z.m_set[iNode3 - 1], iNode1, iNode2, edges);
            }
        }
        int iZ;
        if (nCardinality > 0) {
            iZ = this.maxn() - Z.m_set[nCardinality - 1] - 1;
        }
        else {
            iZ = 0;
        }
        while (iZ >= 0) {
            if (this.isConditionalIndependent(iNode2, iNode1, Z.m_set, nCardinality)) {
                return Z;
            }
            if (nCardinality > 0) {
                Z.m_set[nCardinality - 1] = this.next(Z.m_set[nCardinality - 1], iNode1, iNode2, edges);
            }
            for (iZ = nCardinality - 1; iZ >= 0 && Z.m_set[iZ] >= this.maxn(); iZ = nCardinality - 1) {
                for (iZ = nCardinality - 1; iZ >= 0 && Z.m_set[iZ] >= this.maxn(); --iZ) {}
                if (iZ < 0) {
                    break;
                }
                Z.m_set[iZ] = this.next(Z.m_set[iZ], iNode1, iNode2, edges);
                for (int iNode3 = iZ + 1; iNode3 < nCardinality; ++iNode3) {
                    Z.m_set[iNode3] = this.next(Z.m_set[iNode3 - 1], iNode1, iNode2, edges);
                }
            }
        }
        return null;
    }
    
    int next(int x, final int iNode1, final int iNode2, final boolean[][] edges) {
        ++x;
        while (x < this.maxn() && (!edges[iNode1][x] || !edges[iNode2][x] || x == iNode2)) {
            ++x;
        }
        return x;
    }
    
    void calcVeeNodes(final boolean[][] edges, final boolean[][] arrows, final SeparationSet[][] sepsets) {
        for (int iNode1 = 0; iNode1 < this.maxn(); ++iNode1) {
            for (int iNode2 = 0; iNode2 < this.maxn(); ++iNode2) {
                arrows[iNode1][iNode2] = false;
            }
        }
        for (int iNode1 = 0; iNode1 < this.maxn() - 1; ++iNode1) {
            for (int iNode2 = iNode1 + 1; iNode2 < this.maxn(); ++iNode2) {
                if (!edges[iNode1][iNode2]) {
                    for (int iNode3 = 0; iNode3 < this.maxn(); ++iNode3) {
                        if ((iNode3 != iNode1 && iNode3 != iNode2 && edges[iNode1][iNode3] && edges[iNode2][iNode3]) & !sepsets[iNode1][iNode2].contains(iNode3)) {
                            arrows[iNode1][iNode3] = true;
                            arrows[iNode2][iNode3] = true;
                        }
                    }
                }
            }
        }
    }
    
    void calcArcDirections(final boolean[][] edges, final boolean[][] arrows) {
        boolean bFound;
        do {
            bFound = false;
            for (int i = 0; i < this.maxn(); ++i) {
                for (int j = 0; j < this.maxn(); ++j) {
                    if (i != j && arrows[i][j]) {
                        for (int k = 0; k < this.maxn(); ++k) {
                            if (i != k && j != k && edges[j][k] && !edges[i][k] && !arrows[j][k] && !arrows[k][j]) {
                                arrows[j][k] = true;
                                bFound = true;
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < this.maxn(); ++i) {
                for (int j = 0; j < this.maxn(); ++j) {
                    if (i != j && arrows[i][j]) {
                        for (int k = 0; k < this.maxn(); ++k) {
                            if (i != k && j != k && edges[i][k] && arrows[j][k] && !arrows[i][k] && !arrows[k][i]) {
                                arrows[i][k] = true;
                                bFound = true;
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < this.maxn(); ++i) {
                for (int j = 0; j < this.maxn(); ++j) {
                    if (i != j && arrows[i][j]) {
                        for (int k = 0; k < this.maxn(); ++k) {
                            if (k != i && k != j && arrows[k][j] && !edges[k][i]) {
                                for (int m = 0; m < this.maxn(); ++m) {
                                    if (m != i && m != j && m != k && edges[m][i] && !arrows[m][i] && !arrows[i][m] && edges[m][j] && !arrows[m][j] && !arrows[j][m] && edges[m][k] && !arrows[m][k] && !arrows[k][m]) {
                                        arrows[m][j] = true;
                                        bFound = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < this.maxn(); ++i) {
                for (int j = 0; j < this.maxn(); ++j) {
                    if (i != j && arrows[j][i]) {
                        for (int k = 0; k < this.maxn(); ++k) {
                            if (k != i && k != j && edges[k][j] && !arrows[k][j] && !arrows[j][k] && edges[k][i] && !arrows[k][i] && !arrows[i][k]) {
                                for (int m = 0; m < this.maxn(); ++m) {
                                    if (m != i && m != j && m != k && edges[m][i] && !arrows[m][i] && !arrows[i][m] && edges[m][k] && !arrows[m][k] && !arrows[k][m]) {
                                        arrows[i][m] = true;
                                        arrows[k][m] = true;
                                        bFound = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (!bFound) {
                for (int i = 0; !bFound && i < this.maxn(); ++i) {
                    for (int j = 0; !bFound && j < this.maxn(); ++j) {
                        if (edges[i][j] && !arrows[i][j] && !arrows[j][i]) {
                            arrows[i][j] = true;
                            bFound = true;
                        }
                    }
                }
            }
        } while (bFound);
    }
    
    @Override
    public Enumeration listOptions() {
        final Vector result = new Vector();
        result.addElement(new Option("\tWhen determining whether an edge exists a search is performed \n\tfor a set Z that separates the nodes. MaxCardinality determines \n\tthe maximum size of the set Z. This greatly influences the \n\tlength of the search. (default 2)", "cardinality", 1, "-cardinality <num>"));
        final Enumeration en = super.listOptions();
        while (en.hasMoreElements()) {
            result.addElement(en.nextElement());
        }
        return result.elements();
    }
    
    @Override
    public void setOptions(final String[] options) throws Exception {
        final String tmpStr = Utils.getOption("cardinality", options);
        if (tmpStr.length() != 0) {
            this.setMaxCardinality(Integer.parseInt(tmpStr));
        }
        else {
            this.setMaxCardinality(2);
        }
        super.setOptions(options);
    }
    
    @Override
    public String[] getOptions() {
        final Vector result = new Vector();
        final String[] options = super.getOptions();
        for (int i = 0; i < options.length; ++i) {
            result.add(options[i]);
        }
        result.add("-cardinality");
        result.add("" + this.getMaxCardinality());
        return result.toArray(new String[result.size()]);
    }
    
    public String maxCardinalityTipText() {
        return "When determining whether an edge exists a search is performed for a set Z that separates the nodes. MaxCardinality determines the maximum size of the set Z. This greatly influences the length of the search. Default value is 2.";
    }
    
    @Override
    public String globalInfo() {
        return "This Bayes Network learning algorithm uses conditional independence tests to find a skeleton, finds V-nodes and applies a set of rules to find the directions of the remaining arrows.";
    }
    
    @Override
    public String getRevision() {
        return RevisionUtils.extract("$Revision: 8034 $");
    }
    
    public static void main(final String[] argv) {
        try {
            final BayesNet b = new BayesNet();
            b.setSearchAlgorithm(new ICSSearchAlgorithm());
            final Instances instances = new Instances(new FileReader("C:\\eclipse\\workspace\\weka\\data\\contact-lenses.arff"));
            instances.setClassIndex(instances.numAttributes() - 1);
            b.buildClassifier(instances);
            System.out.println(b.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    class SeparationSet implements RevisionHandler
    {
        public int[] m_set;
        
        public SeparationSet() {
            this.m_set = new int[ICSSearchAlgorithm.this.getMaxCardinality() + 1];
        }
        
        public boolean contains(final int nItem) {
            for (int iItem = 0; iItem < ICSSearchAlgorithm.this.getMaxCardinality() && this.m_set[iItem] != -1; ++iItem) {
                if (this.m_set[iItem] == nItem) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public String getRevision() {
            return RevisionUtils.extract("$Revision: 8034 $");
        }
    }
}
