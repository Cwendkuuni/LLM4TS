
package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Assert;
import org.junit.Test;
import java.util.*;
import static org.mockito.Mockito.*;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws Exception {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);
        
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        // assert result
        Assert.assertNull(result);
    }

    @Test
    public void testFindForMap() throws Exception {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);

        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);

        // assert result
        Assert.assertNull(result);
    }
    
    @Test
    public void testConverter() throws Exception {
        JavaType concreteType = mock(JavaType.class);
        Class<?> rawSuper = List.class;

        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                JavaUtilCollectionsDeserializers.converter(1, concreteType, rawSuper);
        
        // assert converter inputType
        Assert.assertEquals(concreteType, converter.getInputType(TypeFactory.defaultInstance()));

        // assert converter outputType
        Assert.assertEquals(concreteType, converter.getOutputType(TypeFactory.defaultInstance()));
    }

    @Test
    public void testJavaUtilCollectionsConverter_convert_singletonSet() throws Exception {
        Set<?> set = Collections.singleton(Boolean.TRUE);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(1, mock(JavaType.class));

        Object result = converter.convert(set);

        // assert result
        Assert.assertEquals(Collections.singleton(set.iterator().next()), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaUtilCollectionsConverter_convert_invalidSingletonSet() throws Exception {
        Set<?> set = new HashSet<>();
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(1, mock(JavaType.class));

        converter.convert(set);
    }

    @Test
    public void testJavaUtilCollectionsConverter_convert_singletonList() throws Exception {
        List<?> list = Collections.singletonList(Boolean.TRUE);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(2, mock(JavaType.class));

        Object result = converter.convert(list);

        // assert result
        Assert.assertEquals(Collections.singletonList(list.get(0)), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaUtilCollectionsConverter_convert_invalidSingletonList() throws Exception {
        List<?> list = new ArrayList<>();
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(2, mock(JavaType.class));

        converter.convert(list);
    }

    @Test
    public void testJavaUtilCollectionsConverter_convert_singletonMap() throws Exception {
        Map<?,?> map = Collections.singletonMap("a", "b");
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(3, mock(JavaType.class));

        Object result = converter.convert(map);

        // assert result
        Map.Entry<?,?> entry = map.entrySet().iterator().next();
        Assert.assertEquals(Collections.singletonMap(entry.getKey(), entry.getValue()), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaUtilCollectionsConverter_convert_invalidSingletonMap() throws Exception {
        Map<?,?> map = new HashMap<>();
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(3, mock(JavaType.class));

        converter.convert(map);
    }

    @Test
    public void testJavaUtilCollectionsConverter_convert_unmodifiableSet() throws Exception {
        Set<?> set = new HashSet<>(Arrays.asList(1, 2, 3));
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(4, mock(JavaType.class));

        Object result = converter.convert(set);

        // assert result
        Assert.assertEquals(Collections.unmodifiableSet(set), result);
    }

    @Test
    public void testJavaUtilCollectionsConverter_convert_unmodifiableList() throws Exception {
        List<?> list = new ArrayList<>(Arrays.asList(1, 2, 3));
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(5, mock(JavaType.class));

        Object result = converter.convert(list);

        // assert result
        Assert.assertEquals(Collections.unmodifiableList(list), result);
    }

    @Test
    public void testJavaUtilCollectionsConverter_convert_unmodifiableMap() throws Exception {
        Map<?,?> map = new HashMap<>();
        map.put("a", "b");
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(6, mock(JavaType.class));

        Object result = converter.convert(map);

        // assert result
        Assert.assertEquals(Collections.unmodifiableMap(map), result);
    }

    @Test
    public void testJavaUtilCollectionsConverter_convert_typeAsList() throws Exception {
        List<?> list = new ArrayList<>();
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(7, mock(JavaType.class));

        Object result = converter.convert(list);

        // assert result
        Assert.assertEquals(list, result);
    }

}
