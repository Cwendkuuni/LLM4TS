import org.junit.Test;

import static org.junit.Assert.*;

public class AnyGetterWriterTest
{
    @Test
    public void testGetAndSerialize_NullValue()
    {
        AnyGetterWriter writer = new AnyGetterWriter(null, null, null);
        try {
            writer.getAndSerialize(null, null, null);
        } catch (Exception e) {
            fail("Exception thrown when calling getAndSerialize with null value");
        }
    }
    
    @Test
    public void testGetAndSerialize_NonMapValue()
    {
        AnyGetterWriter writer = new AnyGetterWriter(null, null, null);
        try {
            writer.getAndSerialize(new Integer(10), null, null);
            fail("Exception not thrown when calling getAndSerialize with non-Map value");
        } catch (JsonMappingException e) {
            assertEquals("Value returned by 'any-getter' (null()) not java.util.Map but java.lang.Integer", e.getMessage());
        } catch (Exception e) {
            fail("Incorrect exception thrown when calling getAndSerialize with non-Map value");
        }
    }
    
    @Test
    public void testGetAndSerialize_NullMapSerializer()
    {
        AnyGetterWriter writer = new AnyGetterWriter(null, null, null);
        try {
            writer.getAndSerialize(new java.util.HashMap(), null, null);
        } catch (Exception e) {
            fail("Exception thrown when calling getAndSerialize with null MapSerializer");
        }
    }
    
    @Test
    public void testGetAndFilter_NullValue()
    {
        AnyGetterWriter writer = new AnyGetterWriter(null, null, null);
        try {
            writer.getAndFilter(null, null, null, null);
        } catch (Exception e) {
            fail("Exception thrown when calling getAndFilter with null value");
        }
    }
    
    @Test
    public void testGetAndFilter_NonMapValue()
    {
        AnyGetterWriter writer = new AnyGetterWriter(null, null, null);
        try {
            writer.getAndFilter(new Integer(10), null, null, null);
            fail("Exception not thrown when calling getAndFilter with non-Map value");
        } catch (JsonMappingException e) {
            assertEquals("Value returned by 'any-getter' (null()) not java.util.Map but java.lang.Integer", e.getMessage());
        } catch (Exception e) {
            fail("Incorrect exception thrown when calling getAndFilter with non-Map value");
        }
    }
    
    @Test
    public void testResolve()
    {
        AnyGetterWriter writer = new AnyGetterWriter(null, null, null);
        try {
            writer.resolve(null);
        } catch (JsonMappingException e) {
            fail("Exception thrown when calling resolve");
        }
    }
}