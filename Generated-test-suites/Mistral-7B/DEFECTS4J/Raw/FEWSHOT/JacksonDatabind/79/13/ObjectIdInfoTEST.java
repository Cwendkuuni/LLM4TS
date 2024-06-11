package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;

import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfo() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<IdentityInfoGenerator> generator = IdentityInfoGenerator.class;
        Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        ObjectIdInfo withAlwaysAsId = objectIdInfo.withAlwaysAsId(true);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
        assertEquals(true, withAlwaysAsId.getAlwaysAsId());

        assertNotNull(objectIdInfo.toString());
    }

    @Test
    public void testConstructorWithPropertyNameAndScope() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, null, false, null);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertNull(objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithPropertyNameAndScopeAndGenerator() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<IdentityInfoGenerator> generator = IdentityInfoGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, null);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithPropertyNameStringAndGenerator() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<IdentityInfoGenerator> generator = IdentityInfoGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, "scope", generator, false, null);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithPropertyNameStringAndGeneratorAndResolver() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<IdentityInfoGenerator> generator = IdentityInfoGenerator.class;
        Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, "scope", generator, false, resolver);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithPropertyNameStringAndGeneratorAndAlwaysAsId() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<IdentityInfoGenerator> generator = IdentityInfoGenerator.class;
        boolean alwaysAsId = true;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, alwaysAsId, null);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertEquals(alwaysAsId, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithPropertyNameAndGeneratorAndResolver() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<IdentityInfoGenerator> generator = IdentityInfoGenerator.class;
        Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithPropertyNameAndGeneratorAndAlwaysAsId() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<IdentityInfoGenerator> generator = IdentityInfoGenerator.class;
        boolean alwaysAsId = true;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, alwaysAsId, null);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertEquals(alwaysAsId, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithNameAndScopeAndGenerator() {
        String name = "name";
        Class<String> scope = String.class;
        Class<IdentityInfoGenerator> generator = IdentityInfoGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator, false, null);

        assertEquals(new PropertyName(name), objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithNameAndScopeAndGeneratorAndResolver() {
        String name = "name";
        Class<String> scope = String.class;
        Class<IdentityInfoGenerator> generator = IdentityInfoGenerator.class;
        Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator, false, resolver);

        assertEquals(new PropertyName(name), objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithNameAndScopeAndGeneratorAndAlwaysAsId() {
        String name = "name";
        Class<String> scope = String.class;
        Class<IdentityInfoGenerator> generator = IdentityInfoGenerator.class;
        boolean alwaysAsId = true;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator, alwaysAsId, null);

        assertEquals(new PropertyName(name), objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertEquals(alwaysAsId, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithNameAndGenerator() {
        String name = "name";
        Class<IdentityInfoGenerator> generator = IdentityInfoGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, null, generator, false, null);

        assertEquals(new PropertyName(name), objectIdInfo.getPropertyName());
        assertNull(objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithNameAndGeneratorAndResolver() {
        String name = "name";
        Class<IdentityInfoGenerator> generator = IdentityInfoGenerator.class;
        Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, null, generator, false, resolver);

        assertEquals(new PropertyName(name), objectIdInfo.getPropertyName());
        assertNull(objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithNameAndGeneratorAndAlwaysAsId() {
        String name = "name";
        Class<IdentityInfoGenerator> generator = IdentityInfoGenerator.class;
        boolean alwaysAsId = true;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, null, generator, alwaysAsId, null);

        assertEquals(new PropertyName(name), objectIdInfo.getPropertyName());
        assertNull(objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertEquals(alwaysAsId, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithNameAndScopeAndAlwaysAsId() {
        String name = "name";
        Class<String> scope = String.class;
        boolean alwaysAsId = true;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, null, alwaysAsId, null);

        assertEquals(new PropertyName(name), objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertNull(objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertEquals(alwaysAsId, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithNameAndScopeAndGeneratorAndNullResolver() {
        String name = "name";
        Class<String> scope = String.class;
        Class<IdentityInfoGenerator> generator = IdentityInfoGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator, false, null);

        assertEquals(new PropertyName(name), objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
    }
}