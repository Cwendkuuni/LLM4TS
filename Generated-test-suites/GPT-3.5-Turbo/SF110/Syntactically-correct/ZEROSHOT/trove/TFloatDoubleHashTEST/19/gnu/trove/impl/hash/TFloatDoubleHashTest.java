package gnu.trove.impl.hash;

import gnu.trove.procedure.*;
import gnu.trove.impl.*;
import java.io.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals("Default no-entry key should be 0.0f", 0.0f, hash.getNoEntryKey(), 0.0);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals("Default no-entry value should be 0.0", 0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        int initialCapacity = 10;
        int capacity = hash.setUp(initialCapacity);
        assertEquals("Capacity should be set correctly", 16, capacity);
        assertEquals("Set array should be initialized with the correct length", 16, hash._set.length);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(10.0f);
        assertTrue("Should contain inserted key", hash.contains(10.0f));
        assertFalse("Should not contain non-inserted key", hash.contains(20.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(10.0f);
        hash.insertKey(20.0f);
        hash.insertKey(30.0f);
        
        TFloatProcedure procedure = new TFloatProcedure() {
            float sum = 0.0f;
            public boolean execute(float value) {
                sum += value;
                return true;
            }
        };
        
        assertTrue("Should iterate over all values", hash.forEach(procedure));
        assertEquals("Sum of all values should be correct", 60.0f, procedure.sum, 0.0);
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(10.0f);
        hash.removeAt(0);
        assertFalse("Should not contain removed key", hash.contains(10.0f));
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(10.0f);
        assertEquals("Index of inserted key should be correct", 0, hash.index(10.0f));
        assertEquals("Index of non-inserted key should be -1", -1, hash.index(20.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals("Index of inserted key should be correct", 0, hash.insertKey(10.0f));
        assertEquals("Index of duplicate key should be correct", -1, hash.insertKey(10.0f));
    }

    @Test
    public void testXinsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals("Index of inserted key should be correct", 0, hash.XinsertKey(10.0f));
        assertEquals("Index of duplicate key should be correct", -1, hash.XinsertKey(10.0f));
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        
        hash.writeExternal(out);
        out.flush();

        byte[] bytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream in = new ObjectInputStream(bais);
        
        assertEquals("Serialized object should have correct format", 0, in.readByte());
        assertEquals("Serialized object should have correct initial capacity", 10, in.readInt());
        assertEquals("Serialized object should have correct load factor", 0.5, in.readFloat(), 0.0);
        assertEquals("Serialized object should have correct no-entry key", 0.0f, in.readFloat(), 0.0);
        assertEquals("Serialized object should have correct no-entry value", 0.0, in.readDouble(), 0.0);
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        
        hash.writeExternal(out);
        out.flush();

        byte[] bytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream in = new ObjectInputStream(bais);
        
        hash.readExternal(in);

        assertEquals("After serialization and deserialization, no-entry key should be 0.0f", 0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals("After serialization and deserialization, no-entry value should be 0.0", 0.0, hash.getNoEntryValue(), 0.0);
    }

}