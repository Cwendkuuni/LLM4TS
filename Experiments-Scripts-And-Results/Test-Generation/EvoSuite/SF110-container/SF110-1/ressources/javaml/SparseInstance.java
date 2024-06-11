package net.sf.javaml.core;

import java.util.*;

public class SparseInstance extends AbstractInstance implements Instance
{
    private HashMap<Integer, Double> data;
    private double defaultValue;
    private int noAttributes;
    private static final long serialVersionUID = -7642462956857985858L;
    
    public void setNoAttributes(final int noAttributes) {
        this.noAttributes = noAttributes;
    }
    
    public SparseInstance() {
        this(-1);
    }
    
    public SparseInstance(final int noAttributes) {
        this(noAttributes, 0.0, null);
    }
    
    public SparseInstance(final int noAttributes, final double defaultValue) {
        this(noAttributes, defaultValue, null);
    }
    
    public SparseInstance(final int noAttributes, final Object classValue) {
        this(noAttributes, 0.0, classValue);
    }
    
    public SparseInstance(final int noAttributes, final double defaultValue, final Object classValue) {
        super(classValue);
        this.data = new HashMap<Integer, Double>();
        this.noAttributes = -1;
        this.defaultValue = defaultValue;
        this.noAttributes = noAttributes;
    }
    
    public SparseInstance(final double[] datavector) {
        this(datavector, 0.0, null);
    }
    
    public SparseInstance(final double[] datavector, final double defaultValue) {
        this(datavector, defaultValue, null);
    }
    
    public SparseInstance(final double[] datavector, final Object classValue) {
        this(datavector, 0.0, classValue);
    }
    
    public SparseInstance(final double[] datavector, final double defaultValue, final Object classValue) {
        super(classValue);
        this.data = new HashMap<Integer, Double>();
        this.noAttributes = -1;
        this.defaultValue = defaultValue;
        this.initiate(datavector);
    }
    
    private void initiate(final double[] datavector) {
        this.data.clear();
        this.noAttributes = datavector.length;
        for (int i = 0; i < datavector.length; ++i) {
            if (datavector[i] != this.defaultValue) {
                this.put(Integer.valueOf(i), Double.valueOf(datavector[i]));
            }
        }
    }
    
    @Override
    public double value(final int pos) {
        return this.get(pos);
    }
    
    @Override
    public void clear() {
        this.data.clear();
    }
    
    @Override
    public boolean containsKey(final Object key) {
        return this.data.containsKey(key);
    }
    
    @Override
    public boolean containsValue(final Object value) {
        return this.data.containsValue(value);
    }
    
    @Override
    public Set<Map.Entry<Integer, Double>> entrySet() {
        return this.data.entrySet();
    }
    
    @Override
    public Double get(final Object key) {
        if (this.data.containsKey(key)) {
            return this.data.get(key);
        }
        return this.defaultValue;
    }
    
    @Override
    public boolean isEmpty() {
        return this.data.isEmpty();
    }
    
    @Override
    public TreeSet<Integer> keySet() {
        final TreeSet<Integer> set = new TreeSet<Integer>();
        set.addAll(this.data.keySet());
        return set;
    }
    
    @Override
    public Double put(final Integer key, final Double value) {
        return this.data.put(key, value);
    }
    
    @Override
    public void putAll(final Map<? extends Integer, ? extends Double> m) {
        this.data.putAll(m);
    }
    
    @Override
    public Double remove(final Object key) {
        return this.data.remove(key);
    }
    
    @Deprecated
    @Override
    public int size() {
        return this.data.size();
    }
    
    @Override
    public Collection<Double> values() {
        return this.data.values();
    }
    
    @Override
    public int noAttributes() {
        if (this.noAttributes >= 0) {
            return this.noAttributes;
        }
        if (this.data.keySet().size() == 0) {
            return 0;
        }
        return Collections.max((Collection<? extends Integer>)this.data.keySet()) + 1;
    }
    
    @Override
    public void removeAttribute(final int remove) {
        this.data.remove(remove);
        final List<Integer> indices = new Vector<Integer>();
        indices.addAll(this.data.keySet());
        Collections.sort(indices);
        for (int i = 0; i < indices.size(); ++i) {
            final int index = indices.get(i);
            if (index > remove) {
                this.data.put(index - 1, this.data.get(index));
                this.data.remove(index);
            }
        }
        --this.noAttributes;
    }
    
    @Override
    public String toString() {
        return "{" + this.data.toString() + ";" + this.classValue() + "}";
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.data == null) ? 0 : this.data.hashCode());
        final long temp = Double.doubleToLongBits(this.defaultValue);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final SparseInstance other = (SparseInstance)obj;
        if (this.data == null) {
            if (other.data != null) {
                return false;
            }
        }
        else if (!this.data.equals(other.data)) {
            return false;
        }
        return Double.doubleToLongBits(this.defaultValue) == Double.doubleToLongBits(other.defaultValue);
    }
    
    @Override
    public Instance copy() {
        final SparseInstance out = new SparseInstance();
        (out.data = new HashMap<Integer, Double>()).putAll(this.data);
        out.defaultValue = this.defaultValue;
        out.noAttributes = this.noAttributes;
        out.setClassValue(this.classValue());
        return out;
    }
    
    @Override
    public void removeAttributes(final Set<Integer> indices) {
        final List<Integer> indix = new Vector<Integer>();
        indix.addAll(indices);
        Collections.sort(indix);
        for (int i = indix.size() - 1; i >= 0; --i) {
            this.removeAttribute(indix.get(i));
        }
    }
}
