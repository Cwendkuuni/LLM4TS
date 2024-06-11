import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.util.Converter;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws JsonMappingException {
        DeserializationContext context = new DeserializationContext();
        JavaType javaType = new JavaType();
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(context, javaType);
        assertNull(result);
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        DeserializationContext context = new DeserializationContext();
        JavaType javaType = new JavaType();
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(context, javaType);
        assertNull(result);
    }

    @Test
    public void testConverter() {
        JavaType concreteType = new JavaType();
        Class<?> rawSuper = List.class;
        JavaUtilCollectionsConverter result = JavaUtilCollectionsDeserializers.converter(1, concreteType, rawSuper);
        assertEquals(concreteType, result.getInputType(null));
        assertEquals(concreteType, result.getOutputType(null));
    }
}