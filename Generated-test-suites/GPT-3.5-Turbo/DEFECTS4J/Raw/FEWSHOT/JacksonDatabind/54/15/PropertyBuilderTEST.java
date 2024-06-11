import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;

public class PropertyBuilderTest {
    @Test
    public void testGetDefaultBean() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        assertNull(propertyBuilder.getDefaultBean());
    }

    @Test
    public void testGetDefaultValue() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        assertEquals("", propertyBuilder.getDefaultValue(javaType));
    }

    @Test
    public void testGetPropertyDefaultValue() {
        String name = "test";
        AnnotatedMember member = null;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        assertNull(propertyBuilder.getPropertyDefaultValue(name, member, javaType));
    }   
}