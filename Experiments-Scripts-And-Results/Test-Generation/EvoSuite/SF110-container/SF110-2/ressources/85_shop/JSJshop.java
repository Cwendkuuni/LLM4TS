package umd.cs.shop;

import javax.swing.*;
import java.util.*;
import java.io.*;
import java.net.*;

public final class JSJshop
{
    public static boolean corbaToHicap;
    public static JApplet applet;
    private JSPlanningDomain dom;
    private JSPlanningProblem prob;
    private JSListPlanningProblem probSet;
    private JSPlan sol;
    private JSJshopNode tree;
    private JSPairPlanTSListNodes solution;
    
    public static void main(final String[] args) {
        if (args.length < 2 || args.length > 4) {
            JSUtil.println("Usage :");
            JSUtil.println(" java JSJshop <domainDef-file-name> <problemDef-file-name> [number-of-plans]");
            JSUtil.println("[number-of-plans] can be 'one' or 'all'. The default value is 'one'");
            return;
        }
        final String one = "one";
        final String all = "all";
        try {
            for (int i = 2; i < args.length; ++i) {
                if (one.equalsIgnoreCase(args[i])) {
                    JSJshopVars.allPlans = false;
                }
                else if (all.equalsIgnoreCase(args[i])) {
                    JSJshopVars.allPlans = true;
                }
                else {
                    final int level = JSJshopVars.flagLevel = Integer.valueOf(args[i]);
                }
            }
        }
        catch (NumberFormatException e) {
            JSUtil.println("Invalid parameter");
            return;
        }
        new JSJshop(args[0], args[1]);
    }
    
    public JSJshop() {
        this.probSet = new JSListPlanningProblem();
        this.tree = null;
    }
    
    public JSJshop(final String nameDomainFile, final String nameProblemFile) {
        this.probSet = new JSListPlanningProblem();
        this.tree = null;
        JSUtil.println("Reading file " + nameDomainFile);
        if (!this.parserFile(nameDomainFile)) {
            if (!JSJshopVars.flagExit) {
                return;
            }
            System.exit(0);
        }
        JSUtil.println("Domain file parsed successfully");
        JSUtil.println("Reading file " + nameProblemFile);
        if (!this.parserFile(nameProblemFile)) {
            if (!JSJshopVars.flagExit) {
                return;
            }
            System.exit(0);
        }
        JSUtil.println("Problem file parsed successfully");
        for (int k = 0; k < this.probSet.size(); ++k) {
            this.prob = this.probSet.elementAt(k);
            JSUtil.println("Solving Problem :" + this.prob.Name());
            final JSListPairPlanTStateNodes allPlans = this.dom.solveAll(this.prob, JSJshopVars.allPlans);
            if (allPlans.isEmpty()) {
                (this.sol = new JSPlan()).assignFailure();
                this.solution = new JSPairPlanTSListNodes(new JSPairPlanTState(this.sol, new JSTState()), new Vector());
                JSUtil.println("0 plans found");
            }
            else {
                this.solution = allPlans.elementAt(0);
                this.sol = this.solution.planS().plan();
                JSUtil.println(allPlans.size() + " plans found.");
                if (JSJshopVars.flagLevel > 0) {
                    JSUtil.println("********* PLANS *******");
                    for (int i = 0; i < allPlans.size(); ++i) {
                        JSUtil.println("Plan # " + (i + 1));
                        final JSPairPlanTSListNodes pair = allPlans.elementAt(i);
                        pair.planS().plan().printPlan();
                    }
                }
            }
        }
    }
    
    public JSJshop(final String nameFile, final JSTaskAtom pred) {
        this.probSet = new JSListPlanningProblem();
        this.tree = null;
        this.setFile(nameFile, pred);
    }
    
    public JSJshopNode getTree() {
        return this.tree;
    }
    
    public JSPairPlanTSListNodes getSolution() {
        return this.solution;
    }
    
    public JSListLogicalAtoms getAddList() {
        if (this.tree == null) {
            return null;
        }
        return this.solution.planS().tState().addList();
    }
    
    public JSListLogicalAtoms getDeleteList() {
        if (this.tree == null) {
            return null;
        }
        return this.solution.planS().tState().deleteList();
    }
    
    public JSPairPlanTSListNodes setFile(final String nameFile, final JSTaskAtom pred) {
        Vector listNodes = new Vector();
        JSJshopVars.VarCounter = 0;
        this.parserFile(nameFile);
        JSUtil.flag("File parsed");
        this.dom.print();
        JSUtil.flag("<- domain");
        this.prob.print();
        JSUtil.flag("<- problem");
        JSJshopVars.allPlans = false;
        final JSListPairPlanTStateNodes listPairs = this.dom.solveAll(this.prob, JSJshopVars.allPlans);
        if (listPairs.size() == 0) {
            return null;
        }
        final JSPairPlanTSListNodes pair = listPairs.elementAt(0);
        JSUtil.flag("**After planning");
        this.sol = pair.planS().plan();
        if (this.sol.isFailure()) {
            JSUtil.print("*NO* SOLUTION FOUND");
            return null;
        }
        JSUtil.print("SOLUTION FOUND");
        pair.planS().tState().print();
        JSUtil.println("***************FINAL SOLUTION*******");
        this.sol.print();
        JSUtil.println("********* list-tree FINAL SOLUTION*******");
        listNodes = pair.listNodes();
        for (int i = 0; i < listNodes.size(); ++i) {
            final JSJshopNode node = listNodes.elementAt(i);
            node.print2();
        }
        final JSJshopNode node = listNodes.elementAt(listNodes.size() - 1);
        listNodes.removeElement(node);
        this.tree = new JSJshopNode(node, listNodes);
        JSUtil.println("********* tree FINAL SOLUTION*******");
        this.tree.print();
        return this.solution = pair;
    }
    
    public void testParser() {
        try {
            final FileReader fr = new FileReader("farp.shp");
            final StreamTokenizer tokenizer = new StreamTokenizer(fr);
            JSUtil.initParseTable(tokenizer);
            while (tokenizer.nextToken() != -1) {
                if (tokenizer.ttype == -2) {
                    System.err.print(new Double(tokenizer.nval).toString() + " ");
                }
                if (tokenizer.ttype == JSJshopVars.leftPar) {
                    System.err.print("( ");
                }
                if (tokenizer.ttype == JSJshopVars.rightPar) {
                    System.err.print(") ");
                }
                if (tokenizer.ttype == JSJshopVars.colon) {
                    System.err.print(": ");
                }
                if (tokenizer.ttype == JSJshopVars.semicolon) {
                    System.err.print("; ");
                }
                if (tokenizer.ttype == JSJshopVars.apostrophe) {
                    System.err.print("' ");
                }
                if (tokenizer.ttype == JSJshopVars.exclamation) {
                    System.err.print("! ");
                }
                if (tokenizer.ttype == JSJshopVars.interrogation) {
                    System.err.print("? ");
                }
                if (tokenizer.ttype == JSJshopVars.minus) {
                    System.err.print("- ");
                }
                if (tokenizer.ttype == JSJshopVars.lessT) {
                    System.err.print("< ");
                }
                if (tokenizer.ttype == JSJshopVars.equalT) {
                    System.err.print("= ");
                }
                if (tokenizer.ttype == JSJshopVars.greaterT) {
                    System.err.print("> ");
                }
                if (tokenizer.ttype == JSJshopVars.plus) {
                    System.err.print("+ ");
                }
                if (tokenizer.ttype == JSJshopVars.backquote) {
                    System.err.print("` ");
                }
                if (tokenizer.ttype == JSJshopVars.slash) {
                    System.err.print("/ ");
                }
                if (tokenizer.ttype == JSJshopVars.coma) {
                    System.err.print(", ");
                }
                if (tokenizer.ttype == JSJshopVars.astherisk) {
                    System.err.print("* ");
                }
                if (tokenizer.ttype == JSJshopVars.rightBrac) {
                    System.err.print("] ");
                }
                if (tokenizer.ttype == JSJshopVars.leftBrac) {
                    System.err.print("[ ");
                }
                if (tokenizer.ttype == JSJshopVars.verticalL) {
                    System.err.print("| ");
                }
                if (tokenizer.ttype == -3) {
                    System.err.print(tokenizer.sval + " ");
                }
            }
            fr.close();
        }
        catch (Exception e) {
            JSUtil.println("Error reading control parameters: " + e);
            System.exit(1);
        }
    }
    
    public boolean parserFile(final String libraryFile) {
        final String libraryDirectory = ".";
        try {
            final FileReader fr = new FileReader(libraryFile);
            final StreamTokenizer tokenizer = new StreamTokenizer(fr);
            tokenizer.lowerCaseMode(true);
            JSUtil.initParseTable(tokenizer);
            if (fr == null) {
                JSUtil.println("Can not open file : " + libraryFile);
                return false;
            }
            while (tokenizer.nextToken() != -1) {
                this.processToken(tokenizer);
            }
            fr.close();
        }
        catch (IOException e) {
            System.out.println("Error in readFile() : " + e);
            return false;
        }
        catch (JSParserError parserError) {
            System.out.println("Error in parsing file");
            return false;
        }
        return true;
    }
    
    public BufferedReader getBufferedReader(final String dir, final String file) {
        return this.getBufferedReader(dir, file);
    }
    
    public BufferedReader getBufferedReader(final String dir, final String file, final JApplet applet) {
        if (file == null) {
            return null;
        }
        BufferedReader br = null;
        FileInputStream libraryFileInputStream = null;
        InputStream conn = null;
        try {
            if (applet != null) {
                final URL url = this.getAppletURL(file, applet);
                if (url == null) {
                    System.err.println("Util.getBufferedReader() error: cannot get URL");
                    return null;
                }
                conn = url.openStream();
                if (conn == null) {
                    System.err.println("Util.getBufferedReader() error: cannot open URL");
                    return null;
                }
            }
            else {
                libraryFileInputStream = new FileInputStream(dir + File.separator + file);
            }
        }
        catch (IOException e) {
            System.err.println("Error 1 in Util.getBufferedReader : " + e);
            return null;
        }
        if (applet != null) {
            br = new BufferedReader(new InputStreamReader(conn));
        }
        else {
            try {
                br = new BufferedReader(new InputStreamReader(libraryFileInputStream, System.getProperty("file.encoding")));
            }
            catch (UnsupportedEncodingException e2) {
                System.err.println("Error 2 in Util.getBufferedReader : " + e2);
                return null;
            }
        }
        return br;
    }
    
    public URL getAppletURL(final String file, final JApplet applet) {
        try {
            return new URL(applet.getCodeBase() + file);
        }
        catch (MalformedURLException e) {
            return null;
        }
    }
    
    public void processToken(final StreamTokenizer tokenizer) {
        if (tokenizer.ttype != JSJshopVars.leftPar) {
            System.err.println("Line : " + tokenizer.lineno() + " Expected '('");
            throw new JSParserError();
        }
        if (!JSUtil.expectTokenType(-3, tokenizer, "Expected 'defdomain or defproblem' ")) {
            throw new JSParserError();
        }
        tokenizer.pushBack();
        final String w = JSUtil.readWord(tokenizer, "JSJshop>>processToken");
        if (w.equals("%%%")) {
            throw new JSParserError();
        }
        if (w.equalsIgnoreCase("defdomain")) {
            this.dom = new JSPlanningDomain(tokenizer);
            return;
        }
        if (w.equalsIgnoreCase("defproblem")) {
            this.prob = new JSPlanningProblem(tokenizer);
            this.probSet.addElement(this.prob);
            return;
        }
        System.err.println("Line : " + tokenizer.lineno() + " Expecting defdomain or defproblem");
        throw new JSParserError();
    }
    
    public JSPlanningDomain dom() {
        return this.dom;
    }
    
    public JSPlanningProblem prob() {
        return this.prob;
    }
    
    public JSPlan sol() {
        return this.sol;
    }
    
    public JSJshopNode tree() {
        return this.tree;
    }
    
    static {
        JSJshop.corbaToHicap = false;
    }
}
