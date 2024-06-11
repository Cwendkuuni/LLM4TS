package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreatorCollectorTest {

    private ObjectMapper objectMapper;
    private CreatorCollector creatorCollector;

    @BeforeAll
    void setup() throws Exception {
        objectMapper = new ObjectMapper();
        MapperConfig<?> config = objectMapper.getConfig();
        ObjectNode rootNode = ObjectMapperTestUtils.createTestObjectNode();
        BeanDescription beanDesc = objectMapper.readValue(rootNode, BeanDescription.Class.valueOf(TestBean.class));
        creatorCollector = new CreatorCollector(beanDesc, config);
    }

    @Test
    void testDefaultCreator() throws IOException {
        // Given
        AnnotatedWithParams defaultCreator = Mockito.mock(AnnotatedWithParams.class);

        // When
        creatorCollector.setDefaultCreator(defaultCreator);

        // Then
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        assertNotNull(valueInstantiator);
    }

    @Test
    void testStringCreator() throws IOException {
        // Given
        String value = "test";
        Class<String> stringClass = String.class;
        AnnotatedWithParams stringCreator = Mockito.mock(AnnotatedWithParams.class);
        Mockito.when(stringCreator.getParameterType(0)).thenReturn(stringClass);

        // When
        creatorCollector.addStringCreator(stringCreator, false);

        // Then
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        assertNotNull(valueInstantiator);
        assertEquals(String.class, valueInstantiator.getValueType());
        Object createdValue = valueInstantiator.createUsingDefault(objectMapper.getDeserializationContext());
        assertEquals(value, createdValue);
    }

    @Test
    void testIntCreator() throws IOException {
        // Given
        int value = 42;
        AnnotatedWithParams intCreator = Mockito.mock(AnnotatedWithParams.class);
        Mockito.when(intCreator.getParameterType(0)).thenReturn(Integer.TYPE);

        // When
        creatorCollector.addIntCreator(intCreator, false);

        // Then
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        assertNotNull(valueInstantiator);
        assertEquals(Integer.TYPE, valueInstantiator.getValueType());
        Object createdValue = valueInstantiator.createUsingDefault(objectMapper.getDeserializationContext());
        assertEquals(value, createdValue);
    }

    @Test
    void testLongCreator() throws IOException {
        // Given
        long value = 42L;
        AnnotatedWithParams longCreator = Mockito.mock(AnnotatedWithParams.class);
        Mockito.when(longCreator.getParameterType(0)).thenReturn(Long.TYPE);

        // When
        creatorCollector.addLongCreator(longCreator, false);

        // Then
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        assertNotNull(valueInstantiator);
        assertEquals(Long.TYPE, valueInstantiator.getValueType());
        Object createdValue = valueInstantiator.createUsingDefault(objectMapper.getDeserializationContext());
        assertEquals(value, createdValue);
    }

    @Test
    void testDoubleCreator() throws IOException {
        // Given
        double value = 42.0;
        AnnotatedWithParams doubleCreator = Mockito.mock(AnnotatedWithParams.class);
        Mockito.when(doubleCreator.getParameterType(0)).thenReturn(Double.TYPE);

        // When
        creatorCollector.addDoubleCreator(doubleCreator, false);

        // Then
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        assertNotNull(valueInstantiator);
        assertEquals(Double.TYPE, valueInstantiator.getValueType());
        Object createdValue = valueInstantiator.createUsingDefault(objectMapper.getDeserializationContext());
        assertEquals(value, createdValue);
    }

    @Test
    void testBooleanCreator() throws IOException {
        // Given
        boolean value = true;
        AnnotatedWithParams booleanCreator = Mockito.mock(AnnotatedWithParams.class);
        Mockito.when(booleanCreator.getParameterType(0)).thenReturn(Boolean.TYPE);

        // When
        creatorCollector.addBooleanCreator(booleanCreator, false);

        // Then
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        assertNotNull(valueInstantiator);
        assertEquals(Boolean.TYPE, valueInstantiator.getValueType());
        Object createdValue = valueInstantiator.createUsingDefault(objectMapper.getDeserializationContext());
        assertEquals(value, createdValue);
    }

    @Test
    void testDelegatingCreator() throws IOException {
        // Given
        Class<?> delegateClass = TestDelegate.class;
        AnnotatedWithParams delegatingCreator = Mockito.mock(AnnotatedWithParams.class);
        SettableBeanProperty[] delegateArgs = new SettableBeanProperty[1];
        SettableBeanProperty delegateArg = Mockito.mock(SettableBeanProperty.class);
        delegateArg.setValue("test");
        delegateArgs[0] = delegateArg;

        // When
        creatorCollector.addDelegatingCreator(delegatingCreator, false, delegateArgs);

        // Then
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        assertNotNull(valueInstantiator);
        assertEquals(TestDelegate.class, valueInstantiator.getDelegateType());
        Object createdDelegate = valueInstantiator.createUsingDefault(objectMapper.getDeserializationContext());
        assertNotNull(createdDelegate);

        // Given
        ObjectNode delegateNode = ObjectMapperTestUtils.createTestObjectNodeForClass(TestDelegate.class);
        TestDelegate createdDelegateInstance = objectMapper.readValue(delegateNode, TestDelegate.class);
        assertEquals("test", createdDelegateInstance.getStringProperty());
    }

    @Test
    void testPropertyBasedCreator() throws IOException {
        // Given
        AnnotatedWithParams propertyCreator = Mockito.mock(AnnotatedWithParams.class);
        SettableBeanProperty[] propertyBasedArgs = new SettableBeanProperty[2];
        SettableBeanProperty propertyArg1 = Mockito.mock(SettableBeanProperty.class);
        propertyArg1.setValue("test1");
        SettableBeanProperty propertyArg2 = Mockito.mock(SettableBeanProperty.class);
        propertyArg2.setValue("test2");
        propertyBasedArgs[0] = propertyArg1;
        propertyBasedArgs[1] = propertyArg2;

        // When
        creatorCollector.addPropertyCreator(propertyCreator, false, propertyBasedArgs);

        // Then
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        assertNotNull(valueInstantiator);

        // Given
        ObjectNode propertyNode = ObjectMapperTestUtils.createTestObjectNodeForClass(TestBean.class);
        TestBean createdBean = objectMapper.readValue(propertyNode, TestBean.class);
        assertEquals("test1", createdBean.getStringProperty1());
        assertEquals("test2", createdBean.getStringProperty2());
    }

    @Test
    void testDuplicateCreator() throws IOException {
        // Given
        AnnotatedWithParams stringCreator = Mockito.mock(AnnotatedWithParams.class);
        Mockito.when(stringCreator.getParameterType(0)).thenReturn(String.class);
        String value = "test";

        // When
        creatorCollector.addStringCreator(stringCreator, false);
        creatorCollector.addStringCreator(stringCreator, false);

        // Then
        assertThrows(IllegalArgumentException.class, () -> {
            creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        });
    }

    @Test
    void testDuplicateExplicitCreator() throws IOException {
        // Given
        AnnotatedWithParams intCreator = Mockito.mock(AnnotatedWithParams.class);
        Mockito.when(intCreator.getParameterType(0)).thenReturn(Integer.TYPE);
        int value = 42;

        // When
        creatorCollector.addIntCreator(intCreator, true);
        creatorCollector.addIntCreator(intCreator, true);

        // Then
        assertThrows(IllegalArgumentException.class, () -> {
            creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        });
    }

    @Test
    void testDelegatingCreatorWithCollectionType() throws IOException {
        // Given
        Class<?> delegateCollectionClass = TestDelegateCollection.class;
        AnnotatedWithParams delegatingCreator = Mockito.mock(AnnotatedWithParams.class);
        SettableBeanProperty[] delegateArgs = new SettableBeanProperty[1];
        SettableBeanProperty delegateArg = Mockito.mock(SettableBeanProperty.class);
        delegateArg.setValue(new ArrayList<>());
        delegateArgs[0] = delegateArg;

        // When
        creatorCollector.addDelegatingCreator(delegatingCreator, false, delegateArgs);

        // Then
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        assertNotNull(valueInstantiator);
        assertEquals(TestDelegateCollection.class, valueInstantiator.getDelegateType());
        Object createdDelegate = valueInstantiator.createUsingDefault(objectMapper.getDeserializationContext());
        assertNotNull(createdDelegate);

        // Given
        ObjectNode delegateNode = ObjectMapperTestUtils.createTestObjectNodeForClass(TestDelegateCollection.class);
        TestDelegateCollection createdDelegateInstance = objectMapper.readValue(delegateNode, TestDelegateCollection.class);
        assertNotNull(createdDelegateInstance.getDelegates());
        assertTrue(createdDelegateInstance.getDelegates().isEmpty());
    }

    @Test
    void testPropertyBasedCreatorWithMissingProperty() throws IOException {
        // Given
        AnnotatedWithParams propertyCreator = Mockito.mock(AnnotatedWithParams.class);
        SettableBeanProperty[] propertyBasedArgs = new SettableBeanProperty[1];
        SettableBeanProperty propertyArg = Mockito.mock(SettableBeanProperty.class);
        propertyArg.getName(); // Missing value
        propertyBasedArgs[0] = propertyArg;

        // When
        creatorCollector.addPropertyCreator(propertyCreator, false, propertyBasedArgs);

        // Then
        assertThrows(IllegalArgumentException.class, () -> {
            creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        });
    }

    @Test
    void testPropertyBasedCreatorWithDuplicateProperty() throws IOException {
        // Given
        AnnotatedWithParams propertyCreator = Mockito.mock(AnnotatedWithParams.class);
        SettableBeanProperty[] propertyBasedArgs = new SettableBeanProperty[2];
        SettableBeanProperty propertyArg1 = Mockito.mock(SettableBeanProperty.class);
        SettableBeanProperty propertyArg2 = Mockito.mock(SettableBeanProperty.class);
        propertyArg1.getName(); // Missing value
        propertyArg2.getName(); // Missing value
        propertyBasedArgs[0] = propertyArg1;
        propertyBasedArgs[1] = propertyArg2;

        // When
        creatorCollector.addPropertyCreator(propertyCreator, false, propertyBasedArgs);

        // Then
        assertThrows(IllegalArgumentException.class, () -> {
            creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        });
    }
}

class TestBean {
    private String stringProperty;
    private String stringProperty1;
    private String stringProperty2;

    @JsonCreator
    public TestBean(@JsonProperty("strProp1") String stringProperty,
                    @JsonProperty("strProp2") String stringProperty1,
                    @JsonProperty("strProp3") String stringProperty2) {
        this.stringProperty = stringProperty;
        this.stringProperty1 = stringProperty1;
        this.stringProperty2 = stringProperty2;
    }

    public String getStringProperty() {
        return stringProperty;
    }

    public String getStringProperty1() {
        return stringProperty1;
    }

    public String getStringProperty2() {
        return stringProperty2;
    }
}

class TestDelegate {
    private String stringProperty;

    @JsonCreator
    public TestDelegate(@JsonProperty("strProp") String stringProperty) {
        this.stringProperty = stringProperty;
    }

    public String getStringProperty() {
        return stringProperty;
    }
}

class TestDelegateCollection extends ArrayList<TestDelegate> {
}