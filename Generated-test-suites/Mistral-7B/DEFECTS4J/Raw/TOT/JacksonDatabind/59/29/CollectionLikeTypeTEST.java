import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeModifier;

public class CollectionLikeTypeTest {

    @Test
    public void testConstructBasicCollectionType() throws JsonMappingException {
        JavaType javaType = CollectionLikeType.construct(List.class, TypeBindings.emptyBindings(), null, new JavaType[0], JavaType.constructRawListType(Integer.TYPE));
        CollectionLikeType collectionLikeType = (CollectionLikeType) TypeModifier.constructType(javaType);
        assertNotNull(collectionLikeType);
        assertEquals(List.class, collectionLikeType.getRawClass());
        assertEquals(JavaType.constructRawListType(Integer.TYPE), collectionLikeType.getContentType());
        assertEquals(Collections.emptyList(), collectionLikeType.getBindings().getBindings());
    }

    @Test
    public void testConstructCollectionTypeWithTypeBindings() throws JsonMappingException {
        JavaType javaType = CollectionLikeType.construct(List.class, TypeBindings.mapType(List.class, Integer.TYPE), null, new JavaType[0], JavaType.constructRawListType(Integer.TYPE));
        CollectionLikeType collectionLikeType = (CollectionLikeType) TypeModifier.constructType(javaType);
        assertNotNull(collectionLikeType);
        assertEquals(List.class, collectionLikeType.getRawClass());
        assertEquals(JavaType.constructRawListType(Integer.TYPE), collectionLikeType.getContentType());
        assertEquals(TypeBindings.mapType(List.class, Integer.TYPE), collectionLikeType.getBindings());
    }

    @Test
    public void testUpgradeFromSimpleType() throws JsonMappingException {
        JavaType javaType = JavaType.constructRawClassType(List.class);
        JavaType elementType = JavaType.constructRawClassType(Integer.TYPE);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, elementType);
        assertNotNull(collectionLikeType);
        assertEquals(List.class, collectionLikeType.getRawClass());
        assertEquals(JavaType.constructRawListType(Integer.TYPE), collectionLikeType.getContentType());
        assertNull(collectionLikeType.getBindings());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidType() throws JsonMappingException {
        JavaType javaType = JavaType.constructRawClassType(String.class);
        JavaType elementType = JavaType.constructRawClassType(Integer.TYPE);
        CollectionLikeType.upgradeFrom(javaType, elementType);
    }

    @Test
    public void testRefineType() throws JsonMappingException {
        JavaType javaType = JavaType.constructParametrizedType(ArrayList.class, Arrays.asList(Integer.TYPE));
        CollectionLikeType collectionLikeType = (CollectionLikeType) TypeModifier.constructType(javaType);
        JavaType refinedType = collectionLikeType.refine(ArrayList.class, TypeBindings.mapType(ArrayList.class, Integer.TYPE), null, new JavaType[0]);
        assertNotNull(refinedType);
        assertEquals(ArrayList.class, refinedType.getRawClass());
        assertEquals(JavaType.constructRawListType(Integer.TYPE), refinedType.getContentType());
        assertEquals(TypeBindings.mapType(ArrayList.class, Integer.TYPE), refinedType.getBindings());
    }

    @Test
    public void testIsContainerTypeAndIsCollectionLikeType() {
        JavaType javaType = JavaType.constructRawClassType(List.class);
        CollectionLikeType collectionLikeType = (CollectionLikeType) TypeModifier.constructType(javaType);
        assertTrue(collectionLikeType.isContainerType());
        assertTrue(collectionLikeType.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        JavaType javaType = JavaType.constructParametrizedType(ArrayList.class, Arrays.asList(Integer.TYPE));
        CollectionLikeType collectionLikeType = (CollectionLikeType) TypeModifier.constructType(javaType);
        assertEquals(JavaType.constructRawListType(Integer.TYPE), collectionLikeType.getContentType());
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType javaType = JavaType.constructParametrizedType(ArrayList.class, Arrays.asList(Integer.TYPE));
        CollectionLikeType collectionLikeType = (CollectionLikeType) TypeModifier.constructType(javaType);
        assertNull(collectionLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        JavaType javaType = JavaType.constructParametrizedType(ArrayList.class, Arrays.asList(Integer.TYPE));
        CollectionLikeType collectionLikeType = (CollectionLikeType) TypeModifier.constructType(javaType);
        assertFalse(collectionLikeType.hasHandlers());
    }

    @Test
    public void testIsTrueCollectionType() {
        JavaType javaType = ArrayList.class;
        CollectionLikeType collectionLikeType = (CollectionLikeType) TypeModifier.constructType(javaType);
        assertTrue(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        JavaType javaType1 = ArrayList.class;
        JavaType javaType2 = ArrayList.class;
        CollectionLikeType collectionLikeType1 = (CollectionLikeType) TypeModifier.constructType(javaType1);
        CollectionLikeType collectionLikeType2 = (CollectionLikeType) TypeModifier.constructType(javaType2);
        assertTrue(collectionLikeType1.equals(collectionLikeType2));
    }

    @Test
    public void testToString() {
        JavaType javaType = ArrayList.class;
        CollectionLikeType collectionLikeType = (CollectionLikeType) TypeModifier.constructType(javaType);
        assertEquals("[collection-like type; class java.util.ArrayList, contains java.lang.Integer]", collectionLikeType.toString());
    }
}