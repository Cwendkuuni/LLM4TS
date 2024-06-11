package org.apache.commons.collections.primitives;

import java.io.*;

public class ArrayByteList extends RandomAccessByteList implements ByteList, Serializable
{
    private transient byte[] _data;
    private int _size;
    
    public ArrayByteList() {
        this(8);
    }
    
    public ArrayByteList(final int initialCapacity) {
        this._data = null;
        this._size = 0;
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("capacity " + initialCapacity);
        }
        this._data = new byte[initialCapacity];
        this._size = 0;
    }
    
    public ArrayByteList(final ByteCollection that) {
        this(that.size());
        this.addAll(that);
    }
    
    public byte get(final int index) {
        this.checkRange(index);
        return this._data[index];
    }
    
    public int size() {
        return this._size;
    }
    
    public byte removeElementAt(final int index) {
        this.checkRange(index);
        this.incrModCount();
        final byte oldval = this._data[index];
        final int numtomove = this._size - index - 1;
        if (numtomove > 0) {
            System.arraycopy(this._data, index + 1, this._data, index, numtomove);
        }
        --this._size;
        return oldval;
    }
    
    public byte set(final int index, final byte element) {
        this.checkRange(index);
        this.incrModCount();
        final byte oldval = this._data[index];
        this._data[index] = element;
        return oldval;
    }
    
    public void add(final int index, final byte element) {
        this.checkRangeIncludingEndpoint(index);
        this.incrModCount();
        this.ensureCapacity(this._size + 1);
        final int numtomove = this._size - index;
        System.arraycopy(this._data, index, this._data, index + 1, numtomove);
        this._data[index] = element;
        ++this._size;
    }
    
    public void ensureCapacity(final int mincap) {
        this.incrModCount();
        if (mincap > this._data.length) {
            final int newcap = this._data.length * 3 / 2 + 1;
            final byte[] olddata = this._data;
            System.arraycopy(olddata, 0, this._data = new byte[(newcap < mincap) ? mincap : newcap], 0, this._size);
        }
    }
    
    public void trimToSize() {
        this.incrModCount();
        if (this._size < this._data.length) {
            final byte[] olddata = this._data;
            System.arraycopy(olddata, 0, this._data = new byte[this._size], 0, this._size);
        }
    }
    
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(this._data.length);
        for (int i = 0; i < this._size; ++i) {
            out.writeByte(this._data[i]);
        }
    }
    
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this._data = new byte[in.readInt()];
        for (int i = 0; i < this._size; ++i) {
            this._data[i] = in.readByte();
        }
    }
    
    private final void checkRange(final int index) {
        if (index < 0 || index >= this._size) {
            throw new IndexOutOfBoundsException("Should be at least 0 and less than " + this._size + ", found " + index);
        }
    }
    
    private final void checkRangeIncludingEndpoint(final int index) {
        if (index < 0 || index > this._size) {
            throw new IndexOutOfBoundsException("Should be at least 0 and at most " + this._size + ", found " + index);
        }
    }
}
