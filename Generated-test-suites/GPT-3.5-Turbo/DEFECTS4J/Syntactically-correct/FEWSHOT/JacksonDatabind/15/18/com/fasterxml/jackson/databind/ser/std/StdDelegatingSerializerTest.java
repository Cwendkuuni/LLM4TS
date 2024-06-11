import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StdDelegatingSerializerTest {
    
    // test constructors
    @Test
    public void testConstructor1() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new Converter<Object, Object>() {
            public Object convert(Object value) {
                return null;
            }
        });
        
        assertNull(serializer._delegateType);
        assertNull(serializer._delegateSerializer);
    }
    
    @Test
    public void testConstructor2() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(String.class, new Converter<String, Object>() {
            public Object convert(String value) {
                return null;
            }
        });
        
        assertNull(serializer._delegateType);
        assertNull(serializer._delegateSerializer);
    }
    
    @Test
    public void testConstructor3() {
        JavaType delegateType = JavaTypeBuilder.create().build();
        JsonSerializer<?> delegateSerializer = new JsonSerializer<Object>() {
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                // do nothing
            }
        };
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new Converter<Object, Object>() {
            public Object convert(Object value) {
                return null;
            }
        }, delegateType, delegateSerializer);
        
        assertEquals(delegateType, serializer._delegateType);
        assertEquals(delegateSerializer, serializer._delegateSerializer);
    }
    
    // test resolve()
    @Test
    public void testResolve() {
        // TODO: write test case
    }
    
    // test createContextual()
    @Test
    public void testCreateContextual() {
        // TODO: write test case
    }
    
    // test getConverter()
    @Test
    public void testGetConverter() {
        // TODO: write test case
    }
    
    // test getDelegatee()
    @Test
    public void testGetDelegatee() {
        // TODO: write test case
    }
    
    // test serialize()
    @Test
    public void testSerialize() {
        // TODO: write test case
    }
    
    // test serializeWithType()
    @Test
    public void testSerializeWithType() {
        // TODO: write test case
    }
    
    // test isEmpty()
    @Test
    public void testIsEmpty() {
        // TODO: write test case
    }
    
    // test isEmpty()
    @Test
    public void testIsEmpty2() {
        // TODO: write test case
    }
    
    // test getSchema()
    @Test
    public void testGetSchema() {
        // TODO: write test case
    }
    
    // test getSchema()
    @Test
    public void testGetSchema2() {
        // TODO: write test case
    }
    
    // test acceptJsonFormatVisitor()
    @Test
    public void testAcceptJsonFormatVisitor() {
        // TODO: write test case
    }
    
    // test convertValue()
    @Test
    public void testConvertValue() {
        // TODO: write test case
    }
    
    // test helper method
    @Test
    public void testHelperMethod() {
        // TODO: write test case
    }
}