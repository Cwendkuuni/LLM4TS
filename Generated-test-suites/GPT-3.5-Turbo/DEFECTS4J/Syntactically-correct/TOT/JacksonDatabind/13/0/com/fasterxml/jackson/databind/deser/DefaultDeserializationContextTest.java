
package com.fasterxml.jackson.databind.deser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext.Impl context;

    @Before
    public void setup() {
        context = new DefaultDeserializationContext.Impl(null);
    }

    @Test
    public void testFindObjectId_NullObjectIds_EmptyResolverList() {
        // Arrange
        Object id = new Object();
        ObjectIdGenerator<Object> generator = new ObjectIdGenerator<>();
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();

        // Act
        ReadableObjectId objectId = context.findObjectId(id, generator, resolver);

        // Assert
        assertTrue(context._objectIds.containsKey(generator.key(id)));
        assertEquals(resolver, objectId.getResolver());
    }

    @Test
    public void testFindObjectId_ObjectIdsExist_ResolverAlreadyExists() {
        // Arrange
        Object id1 = new Object();
        Object id2 = new Object();
        ObjectIdGenerator<Object> generator = new ObjectIdGenerator<>();
        SimpleObjectIdResolver resolver1 = new SimpleObjectIdResolver();
        SimpleObjectIdResolver resolver2 = new SimpleObjectIdResolver();
        context._objectIdResolvers.add(resolver1);

        // Act
        ReadableObjectId objectId1 = context.findObjectId(id1, generator, resolver1);
        ReadableObjectId objectId2 = context.findObjectId(id2, generator, resolver1);

        // Assert
        assertTrue(context._objectIds.containsKey(generator.key(id1)));
        assertTrue(context._objectIds.containsKey(generator.key(id2)));
        assertEquals(resolver1, objectId1.getResolver());
        assertEquals(resolver1, objectId2.getResolver());
    }

    @Test
    public void testFindObjectId_ObjectIdsExist_ResolverNotFound() {
        // Arrange
        Object id1 = new Object();
        Object id2 = new Object();
        ObjectIdGenerator<Object> generator = new ObjectIdGenerator<>();
        SimpleObjectIdResolver resolver1 = new SimpleObjectIdResolver();
        SimpleObjectIdResolver resolver2 = new SimpleObjectIdResolver();
        context._objectIdResolvers.add(resolver1);

        // Act
        ReadableObjectId objectId1 = context.findObjectId(id1, generator, resolver2);
        ReadableObjectId objectId2 = context.findObjectId(id2, generator, resolver2);

        // Assert
        assertTrue(context._objectIds.containsKey(generator.key(id1)));
        assertTrue(context._objectIds.containsKey(generator.key(id2)));
        assertEquals(resolver2, objectId1.getResolver());
        assertEquals(resolver2, objectId2.getResolver());
    }

    @Test
    public void testFindObjectId_NullObjectIds_NullResolver() {
        // Arrange
        Object id = new Object();
        ObjectIdGenerator<Object> generator = new ObjectIdGenerator<>();

        // Act
        ReadableObjectId objectId = context.findObjectId(id, generator);

        // Assert
        assertTrue(context._objectIds.containsKey(generator.key(id)));
        assertTrue(context._objectIdResolvers.get(0) instanceof SimpleObjectIdResolver);
        assertEquals(SimpleObjectIdResolver.class, objectId.getResolver().getClass());
    }

    @Test
    public void testCheckUnresolvedObjectId_NoUnresolvedObjectIds() {
        // Arrange

        // Act
        context.checkUnresolvedObjectId();

        // Assert (No exception should be thrown)
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectId_UnresolvedObjectIds_ThrowException() {
        // Arrange
        UnresolvedForwardReference exception = new UnresolvedForwardReference("Unresolved forward references for: ");
        context._objectIds.put(new ObjectIdGenerator.IdKey(new Object()), new ReadableObjectId(new ObjectIdGenerator.IdKey(new Object())));
        context._objectIds.get(new ObjectIdGenerator.IdKey(new Object()))._referringProperties = exception.new Forwards();
        
        // Act
        context.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance_DefinitionIsNull_ReturnNull() {
        // Arrange

        // Act
        JsonDeserializer<Object> deserializer = context.deserializerInstance(null, null);

        // Assert
        assertEquals(null, deserializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstance_DefinitionNotClassOrDeserializer_ThrowException() {
        // Arrange
        Object definition = new Object();

        // Act
        context.deserializerInstance(null, definition);

        // Assert (Exception should be thrown)
    }

    @Test(expected = IllegalStateException.class)
    public void testKeyDeserializerInstance_DefinitionNotClassOrKeyDeserializer_ThrowException() {
        // Arrange
        Object definition = new Object();

        // Act
        context.keyDeserializerInstance(null, definition);

        // Assert (Exception should be thrown)
    }
}

