import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext(null);
        JavaType type = new JavaType();
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNull(deserializer);
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext(null);
        JavaType type = new JavaType();
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNull(deserializer);
    }

    @Test
    public void testConverterSingletonSet() {
        JavaType inputType = new JavaType();
        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(1, inputType);
        assertNull(converter.convert(null));
    }

    @Test
    public void testConverterSingletonList() {
        JavaType inputType = new JavaType();
        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(2, inputType);
        assertNull(converter.convert(null));
    }

    @Test
    public void testConverterSingletonMap() {
        JavaType inputType = new JavaType();
        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(3, inputType);
        assertNull(converter.convert(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConverterUnmodifiableSet() {
        JavaType inputType = new JavaType();
        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(4, inputType);
        converter.convert(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConverterUnmodifiableList() {
        JavaType inputType = new JavaType();
        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(5, inputType);
        converter.convert(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConverterUnmodifiableMap() {
        JavaType inputType = new JavaType();
        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(6, inputType);
        converter.convert(null);
    }

    @Test
    public void testConverterAsList() {
        JavaType inputType = new JavaType();
        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(7, inputType);
        assertNull(converter.convert(null));
    }
}