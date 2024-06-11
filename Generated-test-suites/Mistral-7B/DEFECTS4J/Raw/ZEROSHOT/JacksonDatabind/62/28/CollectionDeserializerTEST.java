package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.BaseJsonParser;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.Referring;
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase;
import com.fasterxml.jackson.databind.deser.ValueDeserializer;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.CollectionReferring;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.CollectionReferringAccumulator;
import com.fasterxml.jackson.databind.deser.impl.ValueInstantiator.ValueInstantiatorMatch;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.*;

/**
 * Test file for CollectionDeserializer class.
 */
@DisplayName("CollectionDeserializer Test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CollectionDeserializerTest {

    /**
     * Test instance of CollectionDeserializer.
     */
    private CollectionDeserializer collectionDeserializer;

    /**
     * Test data for deserialization.
     */
    private static final Object TEST_DATA = new Object[][]{
            new Integer[]{1, 2, 3},
            new String[]{"One", "Two", "Three"},
            new Double[]{1.1, 2.2, 3.3},
            new Character[]{'A', 'B', 'C'}
    };

    /**
     * Initialize test instance.
     */
    @BeforeAll
    public void setup() throws Exception {
        JavaType collectionType = TypeFactory.defaultInstance()
                .constructCollectionType(List.class, Object.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        collectionDeserializer = mapper.findValueDeserializer(jsonFactory, collectionType);
    }

    /**
     * Test isCachable method.
     */
    @Test
    @DisplayName("Test isCachable method")
    public void testIsCachable() {
        // Test when valueDeserializer is null
        assertFalse(collectionDeserializer.isCachable());

        // Test when valueDeserializer is not null and has no polymorphic type information
        JsonDeserializer<Integer> intDeserializer = new StandardDeserializer<Integer>(Integer.class);
        collectionDeserializer = collectionDeserializer.withResolved(intDeserializer, intDeserializer, null);
        assertTrue(collectionDeserializer.isCachable());

        // Test when valueDeserializer is not null and has polymorphic type information
        JsonDeserializer<Object> objectDeserializer = new StandardDeserializer<Object>(Object.class);
        collectionDeserializer = collectionDeserializer.withResolved(objectDeserializer, objectDeserializer, null);
        assertFalse(collectionDeserializer.isCachable());
    }

    /**
     * Test createContextual method.
     */
    @Test
    @DisplayName("Test createContextual method")
    public void testCreateContextual() throws Exception {
        // Test with JsonDeserializer for Integer type
        JsonDeserializer<Integer> intDeserializer = new StandardDeserializer<Integer>(Integer.class);
        CollectionDeserializer intCollectionDeserializer =
                (CollectionDeserializer) collectionDeserializer.withResolved(intDeserializer, intDeserializer, null);

        // Test with JsonDeserializer for String type
        JsonDeserializer<String> stringDeserializer = new StandardDeserializer<String>(String.class);
        CollectionDeserializer stringCollectionDeserializer =
                (CollectionDeserializer) collectionDeserializer.withResolved(stringDeserializer, stringDeserializer, null);

        // Test with TypeDeserializer
        TypeDeserializer intTypeDeserializer = new StdTypeDeserializer(Integer.class);
        CollectionDeserializer intTypeCollectionDeserializer =
                (CollectionDeserializer) collectionDeserializer.withResolved(null, null, intTypeDeserializer);

        // Test with ValueInstantiator
        ValueDeserializer intValueDeserializer = new StandardDeserializer<Integer>(Integer.class);
        ValueInstantiator valueInstantiator = new ValueInstantiatorImpl(intValueDeserializer);
        CollectionDeserializer intValueInstantiatorCollectionDeserializer =
                (CollectionDeserializer) collectionDeserializer
                        .withResolved(null, intValueDeserializer, null, null)
                        .withResolved(null, null, valueInstantiator);

        // Test deserialization with different types
        for (Object testData : TEST_DATA) {
            JsonParser testParser = jsonFactory.createParser(mapper.writeValueAsString(testData));
            DeserializationContext testContext = mock(DeserializationContext.class);
            BeanProperty testProperty = mock(BeanProperty.class);

            // Test with Integer type deserializer
            Collection<Integer> intCollection = intCollectionDeserializer.createContextual(testContext, testProperty)
                    .deserialize(testParser, testContext);
            assertArrayEquals((Integer[]) testData, intCollection.toArray(new Integer[0]));

            // Test with String type deserializer
            Collection<String> stringCollection = stringCollectionDeserializer.createContextual(testContext, testProperty)
                    .deserialize(testParser, testContext);
            assertArrayEquals((String[]) testData, stringCollection.toArray(new String[0]));

            // Test with TypeDeserializer
            TypeDeserializer intTypeDeserializerWrapper = new TypeDeserializerWrapper(intTypeDeserializer);
            Collection<Object> intTypeCollection = intTypeCollectionDeserializer.createContextual(testContext, testProperty)
                    .deserialize(testParser, testContext);
            assertArrayEquals(testData, intTypeCollection.toArray(new Object[0]));

            // Test with ValueInstantiator
            Collection<Object> intValueInstantiatorCollection = intValueInstantiatorCollectionDeserializer.createContextual(testContext, testProperty)
                    .deserialize(testParser, testContext);
            assertArrayEquals(testData, intValueInstantiatorCollection.toArray(new Object[0]));
        }
    }

    /**
     * Test deserialize method.
     */
    @Test
    @DisplayName("Test deserialize method")
    public void testDeserialize() throws Exception {
        JsonParser testParser = jsonFactory.createParser(mapper.writeValueAsString(TEST_DATA[0]));

        // Test with JsonDeserializer for Integer type
        Collection<Integer> intCollection = collectionDeserializer.deserialize(testParser, null);
        assertArrayEquals((Integer[]) TEST_DATA[0], intCollection.toArray(new Integer[0]));

        // Test with JsonDeserializer for String type
        JsonDeserializer<String> stringDeserializer = new StandardDeserializer<String>(String.class);
        CollectionDeserializer stringCollectionDeserializer =
                (CollectionDeserializer) collectionDeserializer.withResolved(stringDeserializer, stringDeserializer, null);
        Collection<String> stringCollection = stringCollectionDeserializer.deserialize(testParser, null);
        assertArrayEquals((String[]) TEST_DATA[1], stringCollection.toArray(new String[0]));

        // Test with TypeDeserializer
        TypeDeserializer intTypeDeserializer = new StdTypeDeserializer(Integer.class);
        CollectionDeserializer intTypeCollectionDeserializer =
                (CollectionDeserializer) collectionDeserializer.withResolved(null, null, intTypeDeserializer);
        Collection<Object> intTypeCollection = intTypeCollectionDeserializer.deserialize(testParser, null);
        assertArrayEquals(TEST_DATA[0], intTypeCollection.toArray(new Object[0]));

        // Test with ValueInstantiator
        ValueDeserializer intValueDeserializer = new StandardDeserializer<Integer>(Integer.class);
        ValueInstantiator valueInstantiator = new ValueInstantiatorImpl(intValueDeserializer);
        CollectionDeserializer intValueInstantiatorCollectionDeserializer =
                (CollectionDeserializer) collectionDeserializer
                        .withResolved(null, intValueDeserializer, null, null)
                        .withResolved(null, null, valueInstantiator);
        Collection<Object> intValueInstantiatorCollection = intValueInstantiatorCollectionDeserializer.deserialize(testParser, null);
        assertArrayEquals(TEST_DATA[0], intValueInstantiatorCollection.toArray(new Object[0]));
    }

    /**
     * Test deserializeWithType method.
     */
    @Test
    @DisplayName("Test deserializeWithType method")
    public void testDeserializeWithType() throws Exception {
        JsonParser testParser = jsonFactory.createParser(mapper.writeValueAsString(TEST_DATA[0]));
        TypeDeserializer intTypeDeserializer = new StdTypeDeserializer(Integer.class);

        Collection<Object> collection = collectionDeserializer.deserializeWithType(testParser, null, intTypeDeserializer);
        assertArrayEquals(TEST_DATA[0], collection.toArray(new Object[0]));
    }

    /**
     * Test deserializeWithType method with TypeDeserializerWrapper.
     */
    @Test
    @DisplayName("Test deserializeWithType method with TypeDeserializerWrapper")
    public void testDeserializeWithTypeWithTypeDeserializerWrapper() throws Exception {
        JsonParser testParser = jsonFactory.createParser(mapper.writeValueAsString(TEST_DATA[0]));
        TypeDeserializer intTypeDeserializerWrapper = new TypeDeserializerWrapper(intTypeDeserializer);

        Collection<Object> collection = collectionDeserializer.deserializeWithType(testParser, null, intTypeDeserializerWrapper);
        assertArrayEquals(TEST_DATA[0], collection.toArray(new Object[0]));
    }

    /**
     * Test deserializeWithType method with ValueDeserializer.
     */
    @Test
    @DisplayName("Test deserializeWithType method with ValueDeserializer")
    public void testDeserializeWithTypeWithValueDeserializer() throws Exception {
        JsonParser testParser = jsonFactory.createParser(mapper.writeValueAsString(TEST_DATA[0]));
        ValueDeserializer intValueDeserializer = new StandardDeserializer<Integer>(Integer.class);

        Collection<Object> collection = collectionDeserializer.deserializeWithType(testParser, null, intValueDeserializer);
        assertArrayEquals(TEST_DATA[0], collection.toArray(new Object[0]));
    }

    /**
     * Test handleNonArray method.
     */
    @Test
    @DisplayName("Test handleNonArray method")
    public void testHandleNonArray() throws Exception {
        JsonParser testParser = jsonFactory.createParser(mapper.writeValueAsString(TEST_DATA[1][0]));
        Collection<Object> collection = collectionDeserializer.handleNonArray(testParser, null, null);
        assertEquals(TEST_DATA[1][0], collection.get(0));
    }
}

class TypeDeserializerWrapper implements TypeDeserializer {

    private final TypeDeserializer _typeDeserializer;

    public TypeDeserializerWrapper(TypeDeserializer typeDeserializer) {
        _typeDeserializer = typeDeserializer;
    }

    @Override
    public Object deserializeTypedFromArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return _typeDeserializer.deserializeTypedFromArray(jp, ctxt);
    }

    @Override
    public void resolveValueAndType(JsonParser p, DeserializationContext ctxt, ValueDeserializer vd, JavaType valueType,
            JsonDeserializer<Object> typeDeserializer) throws IOException {
        _typeDeserializer.resolveValueAndType(p, ctxt, vd, valueType, typeDeserializer);
    }
}