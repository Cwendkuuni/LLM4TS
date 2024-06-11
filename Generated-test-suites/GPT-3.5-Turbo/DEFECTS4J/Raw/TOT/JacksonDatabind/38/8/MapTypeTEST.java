
package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class MapTypeTest {

    private MapType mapType;

    @Before
    public void setUp() {
        mapType = MapType.construct(java.util.HashMap.class, JavaType.STRING, JavaType.INTEGER);
    }

    @Test
    public void testConstruct() {
        assertNotNull(mapType);
        assertThat(mapType.getRawClass(), equalTo(java.util.HashMap.class));
        assertThat(mapType.getKeyType(), equalTo(JavaType.STRING));
        assertThat(mapType.getContentType(), equalTo(JavaType.INTEGER));
        assertThat(mapType.getValueType(), not(nullValue()));
    }

    @Test
    public void testWithKeyType() {
        JavaType newKeyType = JavaType.LONG;
        MapType newMapType = mapType.withKeyType(newKeyType);

        assertNotNull(newMapType);
        assertThat(newMapType.getKeyType(), equalTo(newKeyType));
        assertThat(newMapType.getRawClass(), equalTo(java.util.HashMap.class));
        assertThat(newMapType.getContentType(), equalTo(JavaType.INTEGER));
        assertThat(newMapType.getValueType(), not(nullValue()));
    }

    @Test
    public void testWithContentType() {
        JavaType newContentType = JavaType.BOOLEAN;
        MapType newMapType = mapType.withContentType(newContentType);

        assertNotNull(newMapType);
        assertThat(newMapType.getContentType(), equalTo(newContentType));
        assertThat(newMapType.getRawClass(), equalTo(java.util.HashMap.class));
        assertThat(newMapType.getKeyType(), equalTo(JavaType.STRING));
        assertThat(newMapType.getValueType(), not(nullValue()));
    }

    @Test
    public void testWithKeyTypeHandler() {
        Object newTypeHandler = new Object();
        MapType newMapType = mapType.withKeyTypeHandler(newTypeHandler);

        assertNotNull(newMapType);
        assertThat(newMapType.getKeyType().getTypeHandler(), equalTo(newTypeHandler));
        assertThat(newMapType.getContentType(), equalTo(JavaType.INTEGER));
        assertThat(newMapType.getRawClass(), equalTo(java.util.HashMap.class));
        assertThat(newMapType.getValueType(), not(nullValue()));
    }

    @Test
    public void testWithKeyValueHandler() {
        Object newValueHandler = new Object();
        MapType newMapType = mapType.withKeyValueHandler(newValueHandler);

        assertNotNull(newMapType);
        assertThat(newMapType.getContentType(), equalTo(JavaType.INTEGER));
        assertThat(newMapType.getKeyType().getValueHandler(), equalTo(newValueHandler));
        assertThat(newMapType.getRawClass(), equalTo(java.util.HashMap.class));
        assertThat(newMapType.getValueType(), not(nullValue()));
    }

    @Test
    public void testWithStaticTyping() {
        assertFalse(mapType.isStatic());
        MapType newMapType = mapType.withStaticTyping();

        assertNotNull(newMapType);
        assertTrue(newMapType.isStatic());
        assertThat(newMapType.getRawClass(), equalTo(java.util.HashMap.class));
        assertThat(newMapType.getKeyType(), equalTo(JavaType.STRING));
        assertThat(newMapType.getContentType(), equalTo(JavaType.INTEGER));
        assertThat(newMapType.getValueType(), not(nullValue()));
    }

    @Test
    public void testRefine() {
        JavaType superClass = JavaType.LONG;
        JavaType[] superInterfaces = new JavaType[]{JavaType.BOOLEAN};
        MapType newMapType = mapType.refine(java.util.LinkedHashMap.class, null, superClass, superInterfaces);

        assertNotNull(newMapType);
        assertThat(newMapType.getRawClass(), equalTo(java.util.LinkedHashMap.class));
        assertThat(newMapType.getKeyType(), equalTo(JavaType.STRING));
        assertThat(newMapType.getContentType(), equalTo(JavaType.INTEGER));
        assertThat(newMapType.getValueType(), not(nullValue()));
    }

    @Test
    public void testToString() {
        String expectedString = "[map type; class java.util.HashMap, java.lang.String -> java.lang.Integer]";
        assertThat(mapType.toString(), equalTo(expectedString));
    }

}
