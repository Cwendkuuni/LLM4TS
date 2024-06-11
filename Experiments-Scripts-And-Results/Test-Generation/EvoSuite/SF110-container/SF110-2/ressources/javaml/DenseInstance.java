package net.sf.javaml.core;

import java.util.*;

public class DenseInstance extends AbstractInstance implements Instance
{
    private static final long serialVersionUID = 3284511291715269081L;
    private double[] attributes;
    
    public DenseInstance(final double[] att) {
        this(att, null);
    }
    
    public DenseInstance(final double[] att, final Object classValue) {
        super(classValue);
        this.attributes = att.clone();
    }
    
    private DenseInstance() {
    }
    
    public DenseInstance(final int size) {
        this(new double[size]);
    }
    
    @Override
    public double value(final int pos) {
        return this.attributes[pos];
    }
    
    @Override
    public void clear() {
        this.attributes = new double[this.attributes.length];
    }
    
    @Override
    public boolean containsKey(final Object key) {
        if (key instanceof Integer) {
            final int i = (int)key;
            return i >= 0 && i < this.attributes.length;
        }
        return false;
    }
    
    @Override
    public boolean containsValue(final Object value) {
        if (value instanceof Number) {
            final double val = ((Number)value).doubleValue();
            for (int i = 0; i < this.attributes.length; ++i) {
                if (Math.abs(val - this.attributes[i]) < 1.0E-8) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public Set<Map.Entry<Integer, Double>> entrySet() {
        final HashMap<Integer, Double> map = new HashMap<Integer, Double>();
        for (int i = 0; i < this.attributes.length; ++i) {
            map.put(i, this.attributes[i]);
        }
        return map.entrySet();
    }
    
    @Override
    public Double get(final Object key) {
        return this.attributes[(int)key];
    }
    
    @Override
    public boolean isEmpty() {
        return false;
    }
    
    @Override
    public SortedSet<Integer> keySet() {
        final TreeSet<Integer> keys = new TreeSet<Integer>();
        for (int i = 0; i < this.attributes.length; ++i) {
            keys.add(i);
        }
        return keys;
    }
    
    @Override
    public Double put(final Integer key, final Double value) {
        final double val = this.attributes[key];
        this.attributes[key] = value;
        return val;
    }
    
    @Override
    public void putAll(final Map<? extends Integer, ? extends Double> m) {
        for (final Integer key : m.keySet()) {
            this.attributes[key] = (double)m.get(key);
        }
    }
    
    @Override
    public Double remove(final Object key) {
        throw new UnsupportedOperationException("Cannot unset values from a dense instance.");
    }
    
    @Deprecated
    @Override
    public int size() {
        return this.attributes.length;
    }
    
    @Override
    public Collection<Double> values() {
        final Collection<Double> vals = new ArrayList<Double>();
        for (final double v : this.attributes) {
            vals.add(v);
        }
        return vals;
    }
    
    @Override
    public int noAttributes() {
        return this.attributes.length;
    }
    
    @Override
    public String toString() {
        return "{" + Arrays.toString(this.attributes) + ";" + this.classValue() + "}";
    }
    
    @Override
    public void removeAttribute(final int i) {
        final double[] tmp = this.attributes.clone();
        System.arraycopy(tmp, 0, this.attributes = new double[tmp.length - 1], 0, i);
        System.arraycopy(tmp, i + 1, this.attributes, i, tmp.length - i - 1);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + Arrays.hashCode(this.attributes);
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
        final DenseInstance other = (DenseInstance)obj;
        return Arrays.equals(this.attributes, other.attributes);
    }
    
    @Override
    public Instance copy() {
        final DenseInstance out = new DenseInstance();
        out.attributes = this.attributes.clone();
        out.setClassValue(this.classValue());
        return out;
    }
    
    @Override
    public void removeAttributes(final Set<Integer> indices) {
        final double[] tmp = this.attributes.clone();
        this.attributes = new double[tmp.length - indices.size()];
        int index = 0;
        for (int i = 0; i < tmp.length; ++i) {
            if (!indices.contains(i)) {
                this.attributes[index++] = tmp[i];
            }
        }
    }
}
