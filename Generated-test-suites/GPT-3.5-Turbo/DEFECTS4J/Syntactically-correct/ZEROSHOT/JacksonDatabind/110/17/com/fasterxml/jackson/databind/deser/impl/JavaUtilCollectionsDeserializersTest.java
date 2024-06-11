import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import java.util.*;

public class JavaUtilCollectionsDeserializersTest {
    
    @Test
    public void testFindForCollection() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = TypeFactory.defaultInstance().constructType(List.class);
        
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(result);
        assertTrue(result instanceof StdDelegatingDeserializer<?>);
    }
    
    @Test
    public void testFindForMap() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = TypeFactory.defaultInstance().constructType(Map.class);
        
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(result);
        assertTrue(result instanceof StdDelegatingDeserializer<?>);
    }
    
    @Test
    public void testConverter() {
        JavaType concreteType = TypeFactory.defaultInstance().constructType(List.class);
        Class<?> rawSuper = List.class;
        
        JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(7, concreteType, rawSuper);
        assertNotNull(converter);
        assertEquals(concreteType, converter.getInputType(null));
        assertEquals(concreteType, converter.getOutputType(null));
    }
}