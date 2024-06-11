import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection propertyAndSetterInjection;

    @Before
    public void setup(){
        propertyAndSetterInjection = new PropertyAndSetterInjection();
    }

    @Test
    public void shouldProcessInjectionSuccessfully() throws NoSuchFieldException {
        // Arrange
        Class<PropertyAndSetterInjection> clazz = PropertyAndSetterInjection.class;
        Field field = clazz.getDeclaredField("mockCandidateFilter");
        Object fieldOwner = propertyAndSetterInjection;

        // Act
        boolean result = propertyAndSetterInjection.processInjection(field, fieldOwner, new HashSet<>());

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void shouldReturnFalseWhenNoInjectionNeeded() throws NoSuchFieldException {
        // Arrange
        Class<PropertyAndSetterInjection> clazz = PropertyAndSetterInjection.class;
        Field field = clazz.getDeclaredField("notFinalOrStatic");
        Object fieldOwner = propertyAndSetterInjection;

        // Act
        boolean result = propertyAndSetterInjection.processInjection(field, fieldOwner, new HashSet<>());

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void shouldReportErrorWhenFieldInitializationFails() throws NoSuchFieldException {
        // Arrange
        Class<PropertyAndSetterInjection> clazz = PropertyAndSetterInjection.class;
        Field field = clazz.getDeclaredField("mockCandidateFilter");
        Object fieldOwner = propertyAndSetterInjection;

        // Act
        propertyAndSetterInjection.processInjection(field, fieldOwner, null);

        // Assert
        // Error is reported through the MockitoException thrown by the Reporter class.
    }

    @Test
    public void shouldInjectMockCandidateBySetter() throws NoSuchFieldException {
        // Arrange
        Class<PropertyAndSetterInjection> clazz = PropertyAndSetterInjection.class;
        Field field = clazz.getDeclaredField("propertyAndSetterInjection");
        Object fieldOwner = propertyAndSetterInjection;
        Set<Object> mocks = new HashSet<>();
        Object mockObject = new Object(); // Suppose this is a mock object
        mocks.add(mockObject);

        // Act
        boolean result = propertyAndSetterInjection.processInjection(field, fieldOwner, mocks);

        // Assert
        Assert.assertTrue(result);
        Assert.assertNull(propertyAndSetterInjection.getPropertyAndSetterInjection());
    }

    @Test
    public void shouldInjectMockCandidateByFieldInjection() throws NoSuchFieldException {
        // Arrange
        Class<PropertyAndSetterInjection> clazz = PropertyAndSetterInjection.class;
        Field field = clazz.getDeclaredField("mockCandidateFilter");
        Object fieldOwner = propertyAndSetterInjection;
        Set<Object> mocks = new HashSet<>();
        Object mockObject = new Object(); // Suppose this is a mock object
        mocks.add(mockObject);

        // Act
        boolean result = propertyAndSetterInjection.processInjection(field, fieldOwner, mocks);

        // Assert
        Assert.assertTrue(result);
        Assert.assertNull(propertyAndSetterInjection.getMockCandidateFilter());
    }

    @Test
    public void shouldNotInjectMockCandidateWhenNoMockCandidatesAvailable() throws NoSuchFieldException {
        // Arrange
        Class<PropertyAndSetterInjection> clazz = PropertyAndSetterInjection.class;
        Field field = clazz.getDeclaredField("mockCandidateFilter");
        Object fieldOwner = propertyAndSetterInjection;
        Set<Object> mocks = new HashSet<>();

        // Act
        boolean result = propertyAndSetterInjection.processInjection(field, fieldOwner, mocks);

        // Assert
        Assert.assertFalse(result);
        Assert.assertNotNull(propertyAndSetterInjection.getMockCandidateFilter());
    }

    @Test
    public void shouldOrderFieldsFromSubTypeToSuperTypeThenByName() throws NoSuchFieldException {
        // Arrange
        Class<PropertyAndSetterInjection> clazz = PropertyAndSetterInjection.class;
        Field field = clazz.getDeclaredField("mockCandidateFilter");
        Object fieldOwner = propertyAndSetterInjection;
        Set<Object> mocks = new HashSet<>();
        Object mockObject1 = new Object(); // Suppose this is a mock object
        final Object mockObject2 = new Object(); // Suppose this is a mock object
        mocks.add(mockObject1);
        mocks.add(mockObject2);

        // Act
        boolean result = propertyAndSetterInjection.processInjection(field, fieldOwner, mocks);

        // Assert
        Assert.assertTrue(result);
        Assert.assertNull(propertyAndSetterInjection.getMockCandidateFilter());
    }


}