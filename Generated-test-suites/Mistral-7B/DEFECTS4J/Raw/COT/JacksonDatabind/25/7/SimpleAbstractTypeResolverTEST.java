import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.type.ClassIdResolver;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdClassDeserializer.StdResolver;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        Class<List> listClass = List.class;
        Class<LinkedList> linkedListClass = LinkedList.class;

        SimpleAbstractTypeResolver expectedResolver = new SimpleAbstractTypeResolver();
        expectedResolver.addMapping(listClass, linkedListClass);

        assertEquals(expectedResolver, resolver.addMapping(listClass, linkedListClass));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithSameType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        Class<List> listClass = List.class;

        resolver.addMapping(listClass, listClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithInvalidSubType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        Class<List> listClass = List.class;
        Class<String> stringClass = String.class;

        resolver.addMapping(listClass, stringClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithNonAbstractSuperType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        Class<List> listClass = List.class;

        resolver.addMapping(listClass, ArrayList.class);
    }

    @Test
    public void testFindTypeMappingWithEmptyMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        JavaType javaType = mock(JavaType.class);

        assertNull(resolver.findTypeMapping(mock(DeserializationConfig.class), javaType));
    }

    @Test
    public void testFindTypeMappingWithMappedType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        Class<List> listClass = List.class;
        Class<LinkedList> linkedListClass = LinkedList.class;

        when(javaType.getRawClass()).thenReturn(listClass);

        JavaType expectedJavaType = mock(JavaType.class);
        when(expectedJavaType.narrowBy(linkedListClass)).thenReturn(expectedJavaType);

        JavaType actualJavaType = resolver.findTypeMapping(mock(DeserializationConfig.class), javaType);

        assertEquals(expectedJavaType, actualJavaType);
    }

    @Test
    public void testFindTypeMappingWithUnmappedType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        Class<Map> mapClass = Map.class;
        JavaType javaType = mock(JavaType.class);
        when(javaType.getRawClass()).thenReturn(mapClass);

        assertNull(resolver.findTypeMapping(mock(DeserializationConfig.class), javaType));
    }

    @Test(expected = JsonMappingException.class)
    public void testFindTypeMappingWithInvalidSubType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        Class<List> listClass = List.class;
        Class<String> stringClass = String.class;

        JavaType javaType = mock(JavaType.class);
        when(javaType.getRawClass()).thenReturn(listClass);

        resolver.addMapping(listClass, stringClass);

        when(javaType.narrowBy(stringClass)).thenReturn(javaType);

        resolver.findTypeMapping(mock(DeserializationConfig.class), javaType);
    }

    @Test
    public void testResolveAbstractTypeWithEmptyMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        JavaType javaType = mock(JavaType.class);

        assertNull(resolver.resolveAbstractType(mock(DeserializationConfig.class), javaType));
    }
}