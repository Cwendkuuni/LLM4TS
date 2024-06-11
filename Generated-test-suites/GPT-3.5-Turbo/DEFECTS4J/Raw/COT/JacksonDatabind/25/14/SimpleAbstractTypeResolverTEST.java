package com.fasterxml.jackson.databind.module;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Assert;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.core.JsonParser;
import java.lang.Class;
import com.fasterxml.jackson.databind.DeserializationContext;
import java.lang.Exception;
import java.lang.IllegalArgumentException;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.After;
import java.lang.Override;
import org.junit.Before;
import java.lang.Object;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import com.fasterxml.jackson.databind.InjectableValues;
import java.lang.String;
import org.junit.Assume;
import java.lang.reflect.Constructor;

/**
 * Simple {@link AbstractTypeResolver} implementation, which is
 * based on static mapping from abstract super types into
 * sub types (concrete or abstract), but retaining generic
 * parameterization.
 * Can be used for things like specifying which implementation of
 * {@link java.util.Collection} to use:
 *<pre>
 *  SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 *  // To make all properties declared as Collection, List, to LinkedList
 *  resolver.addMapping(Collection.class, LinkedList.class);
 *  resolver.addMapping(List.class, LinkedList.class);
 *</pre>
 * Can also be used as an alternative to per-class annotations when defining
 * concrete implementations; however, only works with abstract types (since
 * this is only called for abstract types)
 */
@RunWith(MockitoJUnitRunner.class)
public class SimpleAbstractTypeResolverTest
{
    private SimpleAbstractTypeResolver simpleAbstractTypeResolver;

    @Before
    public void setup()
    {
        simpleAbstractTypeResolver = new SimpleAbstractTypeResolver();
    }

    @After
    public void tearDown()
    {
        simpleAbstractTypeResolver = null;
    }

    @Test
    public void testFindTypeMapping()
    {
        JavaType baseType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType result = simpleAbstractTypeResolver.findTypeMapping(((DeserializationConfig) null), baseType);
        Assert.assertNull(result);
        Assert.assertSame(baseType, result);
    }

    @Test
    public void testAddMapping_addMapping5()
    {
        final Class<?> _superType = Object.class;
        final Class<?> _subType = Object.class;
        final SimpleAbstractTypeResolver result = simpleAbstractTypeResolver.addMapping(_superType, _subType);
        Assert.assertSame(simpleAbstractTypeResolver, result);
        Assert.assertTrue(this.<ClassKey,Class<?>>getFieldValue("_mappings", this.simpleAbstractTypeResolver).isEmpty());
    }

    @Test
    public void testAddMapping_addMapping7()
    {
        final Class<? extends Object> _superType = Object.class;
        final Class<? extends Object> _subType = Object.class;
        final SimpleAbstractTypeResolver result = simpleAbstractTypeResolver.addMapping(_superType, _subType);
        Assert.assertSame(simpleAbstractTypeResolver, result);
        Assert.assertTrue(this.<ClassKey,Class<?>>getFieldValue("_mappings", this.simpleAbstractTypeResolver).isEmpty());
    }

    @Test
    public void testAddMapping_addMapping9()
    {
        final Class<?> _superType = Object.class;
        final Class<?> _subType = Object.class;
        final SimpleAbstractTypeResolver result = simpleAbstractTypeResolver.addMapping(_superType, _subType);
        Assert.assertSame(simpleAbstractTypeResolver, result);
        Assert.assertTrue(this.<ClassKey,Class<?>>getFieldValue("_mappings", this.simpleAbstractTypeResolver).isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testAddMapping_addMapping11()
    {
        final Class<?> _superType = Object.class;
        final Class<?> _subType = Object.class;
        final SimpleAbstractTypeResolver result = simpleAbstractTypeResolver.addMapping(_superType, _subType);
        Assert.assertSame(simpleAbstractTypeResolver, result);
        Assert.assertTrue(this.<ClassKey,Class<?>>getFieldValue("_mappings", this.simpleAbstractTypeResolver).isEmpty());
    }

    @Test
    public void testResolveAbstractType()
    {
        JavaType result = simpleAbstractTypeResolver.resolveAbstractType(((DeserializationConfig) null), ((JavaType) null));
        Assert.assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testResolveAbstractType13()
    {
        JavaType result = simpleAbstractTypeResolver.resolveAbstractType(((DeserializationConfig) null), ((JavaType) null));
        Assert.assertNull(result);
    }

    @Test
    public void testAddMapping_addMapping15()
    {
        final Class<?> _superType = Object.class;
        final Class<?> _subType = Object.class;
        final SimpleAbstractTypeResolver result = simpleAbstractTypeResolver.addMapping(_superType, _subType);
        Assert.assertSame(simpleAbstractTypeResolver, result);
        Assert.assertTrue(this.<ClassKey,Class<?>>getFieldValue("_mappings", this.simpleAbstractTypeResolver).isEmpty());
    }

    @Test
    public void testResolveAbstractType17()
    {
        JavaType result = simpleAbstractTypeResolver.resolveAbstractType(((DeserializationConfig) null), ((JavaType) null));
        Assert.assertNull(result);
    }

    @Test
    public void testAddMapping_addMapping19()
    {
        final Class<?> _superType = Object.class;
        final Class<?> _subType = Object.class;
        final SimpleAbstractTypeResolver result = simpleAbstractTypeResolver.addMapping(_superType, _subType);
        Assert.assertSame(simpleAbstractTypeResolver, result);
        Assert.assertTrue(this.<ClassKey,Class<?>>getFieldValue("_mappings", this.simpleAbstractTypeResolver).isEmpty());
    }

    @Test
    public void testAddMapping_addMapping21()
    {
        final Class<?> _superType = Object.class;
        final Class<?> _subType = Object.class;
        final SimpleAbstractTypeResolver result = simpleAbstractTypeResolver.addMapping(_superType, _subType);
        Assert.assertSame(simpleAbstractTypeResolver, result);
        Assert.assertTrue(this.<ClassKey,Class<?>>getFieldValue("_mappings", this.simpleAbstractTypeResolver).isEmpty());
    }

    @Test
    public void testAddMapping_addMapping23()
    {
        final Class<?> _superType = Object.class;
        final Class<?> _subType = Object.class;
        final SimpleAbstractTypeResolver result = simpleAbstractTypeResolver.addMapping(_superType, _subType);
        Assert.assertSame(simpleAbstractTypeResolver, result);
        Assert.assertTrue(this.<ClassKey,Class<?>>getFieldValue("_mappings", this.simpleAbstractTypeResolver).isEmpty());
    }

    @Test
    public void testAddMapping_addMapping25()
    {
        final Class<?> _superType = Object.class;
        final Class<?> _subType = Object.class;
        final SimpleAbstractTypeResolver result = simpleAbstractTypeResolver.addMapping(_superType, _subType);
        Assert.assertSame(simpleAbstractTypeResolver, result);
        Assert.assertTrue(this.<ClassKey,Class<?>>getFieldValue("_mappings", this.simpleAbstractTypeResolver).isEmpty());
    }

    private <T, U> T getFieldValue(String fieldName, U instance)
    {
        java.lang.reflect.Field field;
        try
        {
            field = instance.getClass().getDeclaredField(fieldName);
        }
        catch (NoSuchFieldException e)
        {
            throw new IllegalArgumentException("Field doesn't exist", e);
        }
        try
        {
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            T value = (T) field.get(instance);
            field.setAccessible(accessible);
            return value;
        }
        catch (IllegalAccessException e)
        {
            throw new IllegalArgumentException("Can't access field value", e);
        }
    }

    private <T, U> void setFieldValue(String fieldName, U instance, T value)
    {
        java.lang.reflect.Field field;
        try
        {
            field = instance.getClass().getDeclaredField(fieldName);
        }
        catch (NoSuchFieldException e)
        {
            throw new IllegalArgumentException("Field doesn't exist", e);
        }
        try
        {
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            field.set(instance, value);
            field.setAccessible(accessible);
        }
        catch (IllegalAccessException e)
        {
            throw new IllegalArgumentException("Can't set field value", e);
        }
    }
}