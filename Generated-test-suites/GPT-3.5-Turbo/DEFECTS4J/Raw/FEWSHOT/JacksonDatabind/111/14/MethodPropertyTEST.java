import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MethodPropertyTest {
    private MethodProperty methodProperty;

    @Before
    public void setUp() {
        // create a mock AnnotatedMethod for testing
        AnnotatedMethod annotatedMethod = new AnnotatedMethod(MockClass.class, "setMockValue");
        // create a mock BeanPropertyDefinition for testing
        BeanPropertyDefinition propertyDefinition = new BeanPropertyDefinition(null, null, annotatedMethod, null, null, null, null, null);
        // create a mock JavaType for testing
        JavaType javaType = new JavaType(MockClass.class);
        
        methodProperty = new MethodProperty(propertyDefinition, javaType, null, null, annotatedMethod);
    }

    @Test
    public void testWithNullProvider() {
        NullsConstantProvider nullProvider = new NullsConstantProvider(NullsConstantProvider.Nop.NOP);
        MethodProperty newMethodProperty = (MethodProperty) methodProperty.withNullProvider(nullProvider);
        assertEquals(nullProvider, newMethodProperty.getNullValueProvider());
    }

    @Test
    public void testWithDeserializer() {
        JsonDeserializer<?> deserializer = new MockDeserializer();
        MethodProperty newMethodProperty = (MethodProperty) methodProperty.withValueDeserializer(deserializer);
        assertEquals(deserializer, newMethodProperty.getValueDeserializer());
    }

    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("newName");
        MethodProperty newMethodProperty = (MethodProperty) methodProperty.withName(newName);
        assertEquals(newName, newMethodProperty.getFullName());
    }

    @Test
    public void testDeserializeAndSet() {
        MockDeserializer deserializer = new MockDeserializer();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        methodProperty.setValueDeserializer(deserializer);
        methodProperty.setNullValueProvider(new NullsConstantProvider(NullsConstantProvider.Nop.NOP));

        MockClass mock = new MockClass();
        methodProperty.deserializeAndSet(null, null, mock);
        
        assertEquals("mockValue", mock.getMockValue());
    }

    @Test
    public void testDeserializeSetAndReturn() {
        MockDeserializer deserializer = new MockDeserializer();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        methodProperty.setValueDeserializer(deserializer);
        methodProperty.setNullValueProvider(new NullsConstantProvider(NullsConstantProvider.Nop.NOP));

        MockClass mock = new MockClass();
        Object result = methodProperty.deserializeSetAndReturn(null, null, mock);
        
        assertEquals("mockValue", ((MockClass) result).getMockValue());
    }

    @Test
    public void testSet() {
        MockClass mock = new MockClass();
        Object value = "mockValue";
        methodProperty.set(mock, value);
        
        assertEquals(value, mock.getMockValue());
    }

    @Test
    public void testSetAndReturn() {
        MockClass mock = new MockClass();
        Object value = "mockValue";
        Object result = methodProperty.setAndReturn(mock, value);
        
        assertEquals(value, ((MockClass) result).getMockValue());
    }
    
    private static class MockClass {
        private String mockValue;
        
        public void setMockValue(String value) {
            this.mockValue = value;
        }
        
        public String getMockValue() {
            return mockValue;
        }
    }
    
    private static class MockDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return "mockValue";
        }
    }
}