import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class InnerClassPropertyTest {

    private InnerClassProperty innerClassProperty;

    @Before
    public void setUp() {
        innerClassProperty = new InnerClassProperty(null, null);
    }

    @Test
    public void testWithPropertyName() {
        PropertyName newName = new PropertyName("newName");
        InnerClassProperty result = innerClassProperty.withName(newName);
        assertEquals(newName, result.getPropertyName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) {
                return null;
            }
        };
        InnerClassProperty result = innerClassProperty.withValueDeserializer(deser);
        assertEquals(deser, result.getValueDeserializer());
    }

    @Test
    public void testAssignIndex() {
        innerClassProperty.assignIndex(5);
        assertEquals(5, innerClassProperty.getPropertyIndex());
    }

    // Add more test cases for other methods as needed

}