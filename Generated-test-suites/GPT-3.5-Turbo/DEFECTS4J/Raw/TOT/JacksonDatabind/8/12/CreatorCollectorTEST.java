import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;

    @Before
    public void setUp() {
        creatorCollector = new CreatorCollector(mock(BeanDescription.class), true);
    }

    @Test
    public void testSetDefaultCreator() {
        AnnotatedWithParams defaultCreator = mock(AnnotatedWithParams.class);
        creatorCollector.setDefaultCreator(defaultCreator);
        assertEquals(defaultCreator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }

    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams stringCreator = mock(AnnotatedWithParams.class);
        creatorCollector.addStringCreator(stringCreator, true);
        assertEquals(stringCreator, creatorCollector._creators[CreatorCollector.C_STRING]);
    }

    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams intCreator = mock(AnnotatedWithParams.class);
        creatorCollector.addIntCreator(intCreator, true);
        assertEquals(intCreator, creatorCollector._creators[CreatorCollector.C_INT]);
    }

    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams longCreator = mock(AnnotatedWithParams.class);
        creatorCollector.addLongCreator(longCreator, true);
        assertEquals(longCreator, creatorCollector._creators[CreatorCollector.C_LONG]);
    }

    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams doubleCreator = mock(AnnotatedWithParams.class);
        creatorCollector.addDoubleCreator(doubleCreator, true);
        assertEquals(doubleCreator, creatorCollector._creators[CreatorCollector.C_DOUBLE]);
    }

    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams booleanCreator = mock(AnnotatedWithParams.class);
        creatorCollector.addBooleanCreator(booleanCreator, true);
        assertEquals(booleanCreator, creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
    }

    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams delegatingCreator = mock(AnnotatedWithParams.class);
        CreatorProperty[] injectables = new CreatorProperty[1];
        creatorCollector.addDelegatingCreator(delegatingCreator, true, injectables);
        assertEquals(delegatingCreator, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
        assertEquals(injectables, creatorCollector._delegateArgs);
    }

    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams propertyCreator = mock(AnnotatedWithParams.class);
        CreatorProperty[] properties = new CreatorProperty[1];
        creatorCollector.addPropertyCreator(propertyCreator, true, properties);
        assertEquals(propertyCreator, creatorCollector._creators[CreatorCollector.C_PROPS]);
        assertEquals(properties, creatorCollector._propertyBasedArgs);
    }

    @Test
    public void testAddPropertiesWithDuplicateNames() {
        AnnotatedWithParams propertyCreator = mock(AnnotatedWithParams.class);
        CreatorProperty[] properties = new CreatorProperty[2];
        properties[0] = mockCreatorProperty("property1");
        properties[1] = mockCreatorProperty("property1");
        try {
            creatorCollector.addPropertyCreator(propertyCreator, true, properties);
        } catch (IllegalArgumentException e) {
            assertEquals("Duplicate creator property \"property1\" (index 0 vs 1)", e.getMessage());
        }
    }

    @Test
    public void testAddIncompleteParameter() {
        AnnotatedParameter parameter = mock(AnnotatedParameter.class);
        creatorCollector.addIncompeteParameter(parameter);
        assertEquals(parameter, creatorCollector._incompleteParameter);
    }

    @Test
    public void testHasDefaultCreator() {
        creatorCollector.setDefaultCreator(mock(AnnotatedWithParams.class));
        assertEquals(true, creatorCollector.hasDefaultCreator());
    }

    @Test
    public void testConstructValueInstantiator() {
        creatorCollector.setDefaultCreator(mock(AnnotatedWithParams.class));
        creatorCollector.addStringCreator(mock(AnnotatedWithParams.class), true);
        creatorCollector.addIntCreator(mock(AnnotatedWithParams.class), true);
        creatorCollector.addLongCreator(mock(AnnotatedWithParams.class), true);
        creatorCollector.addDoubleCreator(mock(AnnotatedWithParams.class), true);
        creatorCollector.addBooleanCreator(mock(AnnotatedWithParams.class), true);
        DeserializationConfig config = mock(DeserializationConfig.class);
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(config);
        assertEquals(config, valueInstantiator._config);
    }

    private <T> T mock(Class<T> classToMock) {
        try {
            Constructor<T> constructor = classToMock.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private CreatorProperty mockCreatorProperty(String name) {
        CreatorProperty creatorProperty = mock(CreatorProperty.class);

        when(creatorProperty.getName()).thenReturn(name);
        when(creatorProperty.getInjectableValueId()).thenReturn(name);

        return creatorProperty;
    }

}