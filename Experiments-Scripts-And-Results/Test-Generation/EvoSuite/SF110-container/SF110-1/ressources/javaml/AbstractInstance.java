package net.sf.javaml.core;

import java.util.*;

public abstract class AbstractInstance implements Instance
{
    private static final long serialVersionUID = -1712202124913999825L;
    static int nextID;
    private final int ID;
    private Object classValue;
    
    @Override
    public int getID() {
        return this.ID;
    }
    
    @Override
    public Iterator<Double> iterator() {
        return new InstanceValueIterator();
    }
    
    protected AbstractInstance() {
        this(null);
    }
    
    protected AbstractInstance(final Object classValue) {
        this.ID = AbstractInstance.nextID;
        ++AbstractInstance.nextID;
        this.classValue = classValue;
    }
    
    @Override
    public Object classValue() {
        return this.classValue;
    }
    
    @Override
    public void setClassValue(final Object classValue) {
        this.classValue = classValue;
    }
    
    @Override
    public Instance minus(final Instance min) {
        final Instance out = new DenseInstance(new double[this.noAttributes()]);
        for (int i = 0; i < this.noAttributes(); ++i) {
            out.put(i, ((Map<K, Double>)this).get(i) - ((Map<K, Double>)min).get(i));
        }
        return out;
    }
    
    @Override
    public Instance minus(final double min) {
        final Instance out = new DenseInstance(new double[this.noAttributes()]);
        for (int i = 0; i < this.noAttributes(); ++i) {
            out.put(i, ((Map<K, Double>)this).get(i) - min);
        }
        return out;
    }
    
    @Override
    public Instance divide(final double min) {
        final Instance out = new DenseInstance(new double[this.noAttributes()]);
        for (int i = 0; i < this.noAttributes(); ++i) {
            out.put(i, ((Map<K, Double>)this).get(i) / min);
        }
        return out;
    }
    
    @Override
    public Instance multiply(final double value) {
        final Instance out = new DenseInstance(new double[this.noAttributes()]);
        for (int i = 0; i < this.noAttributes(); ++i) {
            out.put(i, ((Map<K, Double>)this).get(i) * value);
        }
        return out;
    }
    
    @Override
    public int hashCode() {
        return this.ID;
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
        final AbstractInstance other = (AbstractInstance)obj;
        return this.ID == other.ID;
    }
    
    @Override
    public Instance multiply(final Instance value) {
        final Instance out = new DenseInstance(new double[this.noAttributes()]);
        for (int i = 0; i < this.noAttributes(); ++i) {
            out.put(i, ((Map<K, Double>)this).get(i) * ((Map<K, Double>)value).get(i));
        }
        return out;
    }
    
    @Override
    public Instance divide(final Instance min) {
        final Instance out = new DenseInstance(new double[this.noAttributes()]);
        for (int i = 0; i < this.noAttributes(); ++i) {
            out.put(i, ((Map<K, Double>)this).get(i) / ((Map<K, Double>)min).get(i));
        }
        return out;
    }
    
    @Override
    public Instance add(final double min) {
        final Instance out = new DenseInstance(new double[this.noAttributes()]);
        for (int i = 0; i < this.noAttributes(); ++i) {
            out.put(i, ((Map<K, Double>)this).get(i) + min);
        }
        return out;
    }
    
    @Override
    public Instance add(final Instance min) {
        final Instance out = new DenseInstance(new double[this.noAttributes()]);
        for (int i = 0; i < this.noAttributes(); ++i) {
            out.put(i, ((Map<K, Double>)this).get(i) + ((Map<K, Double>)min).get(i));
        }
        return out;
    }
    
    @Override
    public Instance sqrt() {
        final Instance out = new DenseInstance(new double[this.noAttributes()]);
        for (int i = 0; i < this.noAttributes(); ++i) {
            out.put(i, Math.sqrt(((Map<K, Double>)this).get(i)));
        }
        return out;
    }
    
    static {
        AbstractInstance.nextID = 0;
    }
    
    class InstanceValueIterator implements Iterator<Double>
    {
        private int index;
        
        InstanceValueIterator() {
            this.index = 0;
        }
        
        @Override
        public boolean hasNext() {
            return this.index < AbstractInstance.this.noAttributes() - 1;
        }
        
        @Override
        public Double next() {
            ++this.index;
            return AbstractInstance.this.value(this.index - 1);
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException("Cannot remove from instance using the iterator.");
        }
    }
}
