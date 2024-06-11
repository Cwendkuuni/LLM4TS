import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import java.util.*;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = new JavaType();
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNull(result);
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = new JavaType();
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNull(result);
    }

    @Test
    public void testConverter() {
        int kind = 1;
        JavaType concreteType = new JavaType();
        Class<?> rawSuper = List.class;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter result = JavaUtilCollectionsDeserializers.converter(kind, concreteType, rawSuper);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvert() {
        Object value = null;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(1, new JavaType());
        Object result = converter.convert(value);
        assertNull(result);
    }

    @Test
    public void testGetInputType() {
        TypeFactory typeFactory = new TypeFactory();
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(1, new JavaType());
        JavaType result = converter.getInputType(typeFactory);
        assertNotNull(result);
    }

    @Test
    public void testGetOutputType() {
        TypeFactory typeFactory = new TypeFactory();
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(1, new JavaType());
        JavaType result = converter.getOutputType(typeFactory);
        assertNotNull(result);
    }

    @Test
    public void testJavaUtilCollectionsDeserializersConstructor() {
        JavaUtilCollectionsDeserializers result = new JavaUtilCollectionsDeserializers() {
        };
        assertNotNull(result);
    }
}