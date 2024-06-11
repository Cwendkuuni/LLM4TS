import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        Assert.assertEquals(null, result);
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        Assert.assertEquals(null, result);
    }
    
    @Test
    public void testConverter() {
        int kind = 0;
        JavaType concreteType = null;
        Class<?> rawSuper = null;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(kind, concreteType, rawSuper);
        Assert.assertNotNull(converter);
    }

    @Test
    public void testConvert() {
        Object value = null;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(0, null);
        Object result = converter.convert(value);
        Assert.assertNull(result);
    }

    @Test
    public void testGetInputType() {
        TypeFactory typeFactory = null;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(0, null);
        JavaType inputType = converter.getInputType(typeFactory);
        Assert.assertNull(inputType);
    }

    @Test
    public void testGetOutputType() {
        TypeFactory typeFactory = null;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(0, null);
        JavaType outputType = converter.getOutputType(typeFactory);
        Assert.assertNull(outputType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckSingleton() {
        int size = 0;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(0, null);
        converter._checkSingleton(size);
    }
}