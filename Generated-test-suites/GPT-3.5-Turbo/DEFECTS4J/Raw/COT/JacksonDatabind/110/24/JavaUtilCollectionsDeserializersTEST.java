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
    public void testFindForCollection() {
        DeserializationContext ctxt = null;
        JavaType type = null;
        try {
            JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindForMap() {
        DeserializationContext ctxt = null;
        JavaType type = null;
        try {
            JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConverter() {
        int kind = 0;
        JavaType concreteType = null;
        Class<?> rawSuper = null;
        JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(kind, concreteType, rawSuper);
    }

}