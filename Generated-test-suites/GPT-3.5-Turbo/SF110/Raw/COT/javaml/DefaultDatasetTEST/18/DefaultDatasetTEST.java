
package net.sf.javaml.core;

import net.sf.javaml.distance.*;
import java.util.*;

public class DefaultDataset extends Vector<Instance> implements Dataset
{
    private int maxAttributes;
    private static final long serialVersionUID = 8586030444860912681L;
    private TreeSet<Object> classes;
    
    public DefaultDataset(final Collection<Instance> coll) {
        this.maxAttributes = 0;
        this.classes = new TreeSet<Object>();
        this.addAll(coll);
    }
    
    public DefaultDataset() {
        this.maxAttributes = 0;
        this.classes = new TreeSet<Object>();
    }
    
    private void check(final Collection<? extends Instance> c) {
        for (final Instance i : c) {
            this.check(i);
        }
    }
    
    private void check(final Instance i) {
        if (i.classValue() != null) {
            this.classes.add(i.classValue());
        }
        if (i.noAttributes() > this.maxAttributes) {
            this.maxAttributes = i.noAttributes();
        }
    }
    
    @Override
    public synchronized boolean addAll(final Collection<? extends Instance> c) {
        this.check(c);
        return super.addAll(c);
    }
    
    @Override
    public synchronized boolean addAll(final int index, final Collection<? extends Instance> c) {
        this.check(c);
        return super.addAll(index, c);
    }
    
    @Override
    public void clear() {
        this.classes.clear();
        super.clear();
    }
    
    @Override
    public synchronized boolean add(final Instance e) {
        this.check(e);
        return super.add(e);
    }
    
    @Override
    public void add(final int index, final Instance e) {
        this.check(e);
        super.add(index, e);
    }
    
    @Override
    public synchronized void addElement(final Instance e) {
        this.check(e);
        super.addElement(e);
    }
    
    @Override
    public synchronized void insertElementAt(final Instance e, final int index) {
        this.check(e);
        super.insertElementAt(e, index);
    }
    
    @Override
    public synchronized void setElementAt(final Instance e, final int index) {
        this.check(e);
        super.setElementAt(e, index);
    }
    
    @Override
    public Instance instance(final int index) {
        return super.get(index);
    }
    
    @Override
    public SortedSet<Object> classes() {
        return this.classes;
    }
    
    @Override
    public Set<Instance> kNearest(final int k, final Instance inst, final DistanceMeasure dm) {
        final Map<Instance, Double> closest = new HashMap<Instance, Double>();
        double max = Double.POSITIVE_INFINITY;
        for (final Instance tmp : this) {
            final double d = dm.measure(inst, tmp);
            if (dm.compare(d, max) && !inst.equals(tmp)) {
                closest.put(tmp, d);
                if (closest.size() <= k) {
                    continue;
                }
                max = this.removeFarthest(closest);
            }
        }
        return closest.keySet();
    }
    
    private double removeFarthest(final Map<Instance, Double> vector) {
        Instance tmp = null;
        double max = 0.0;
        for (final Instance inst : vector.keySet()) {
            final double d = vector.get(inst);
            if (d > max) {
                max = d;
                tmp = inst;
            }
        }
        vector.remove(tmp);
        return max;
    }
    
    @Override
    public Dataset[] folds(final int numFolds, final Random rg) {
        final Dataset[] out = new Dataset[numFolds];
        final List<Integer> indices = new Vector<Integer>();
        for (int i = 0; i < this.size(); ++i) {
            indices.add(i);
        }
        final int size = this.size() / numFolds + 1;
        final int[][] array = new int[numFolds][size];
        for (int j = 0; j < size; ++j) {
            for (int k = 0; k < numFolds; ++k) {
                if (indices.size() > 0) {
                    array[k][j] = indices.remove(rg.nextInt(indices.size()));
                }
                else {
                    array[k][j] = -1;
                }
            }
        }
        for (int j = 0; j < numFolds; ++j) {
            int[] indi;
            if (array[j][size - 1] == -1) {
                indi = new int[size - 1];
                System.arraycopy(array[j], 0, indi, 0, size - 1);
            }
            else {
                indi = new int[size];
                System.arraycopy(array[j], 0, indi, 0, size);
            }
            out[j] = new Fold(this, indi);
        }
        return out;
    }
    
    @Override
    public int noAttributes() {
        if (this.size() == 0) {
            return 0;
        }
        return this.get(0).noAttributes();
    }
    
    @Override
    public int classIndex(final Object clazz) {
        if (clazz != null) {
            return this.classes().headSet(clazz).size();
        }
        return -1;
    }
    
    @Override
    public Object classValue(final int index) {
        int i = 0;
        for (final Object o : this.classes) {
            if (i == index) {
                return o;
            }
            ++i;
        }
        return null;
    }
    
    @Override
    public Dataset copy() {
        final DefaultDataset out = new DefaultDataset();
        for (final Instance i : this) {
            out.add(i.copy());
        }
        return out;
    }
}