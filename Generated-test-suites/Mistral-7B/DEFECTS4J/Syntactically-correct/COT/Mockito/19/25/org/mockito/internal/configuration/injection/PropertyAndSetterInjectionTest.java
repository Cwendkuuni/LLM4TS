import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.util.collections.Sets;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.Field;
import org.mockito.internal.util.reflection.Class;
import org.mockito.internal.util.reflection.List;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@RunWith(org.mockito.runners.JUnit4.class)
public class PropertyAndSetterInjectionTest {

 private PropertyAndSetterInjection injection = new PropertyAndSetterInjection();

 @Test
 public void testProcessInjection_fieldIsInitialized() {
 Field field = Mockito.spy(new Field() {
 @Override
 public FieldType getType() {
 return FieldType.MOCK;
 }
 });
 Object fieldOwner = mock(Object.class);
 Set<Object> mockCandidates = Sets.newMockSafeHashSet(mock(Field.class));

 FieldInitializationReport report = injection.initializeInjectMocksField(field, fieldOwner);
 assertNotNull(report);
 }

 @Test(expected = MockitoException.class)
 public void testProcessInjection_initializationThrowsException() {
 Field field = Mockito.spy(new Field() {
 @Override
 public FieldType getType() {
 return FieldType.MOCK;
 }

 @Override
 public Object get(Object obj) throws IllegalAccessException, InvocationTargetException {
 throw new MockitoException("Cannot initialize field.");
 }
 });
 Object fieldOwner = mock(Object.class);
 Set<Object> mockCandidates = Sets.newMockSafeHashSet(mock(Field.class));

 injection.initializeInjectMocksField(field, fieldOwner);
 }

 @Test
 public void testProcessInjection_noMockCandidatesFound() {
 Field field = new Field() {
 @Override
 public FieldType getType() {
 return FieldType.MOCK;
 }
 };
 Object fieldOwner = mock(Object.class);
 Set<Object> mockCandidates = Sets.newMockSafeHashSet();

 assertFalse(injection.processInjection(field, fieldOwner, mockCandidates));
 }

 @Test
 public void testProcessInjection_mockCandidateFoundByType() {
 Class<?> awaitingInjectionClazz = mock(Class.class);
 Object mockCandidate = mock(Field.class);
 Set<Object> mocks = Sets.newMockSafeHashSet(mockCandidate);
 Object instance = mock(Object.class);

 Field field = new Field() {
 @Override
 public FieldType getType() {
 return FieldType.MOCK;
 }

 @Override
 public Object getType() {
 return awaitingInjectionClazz;
 }
 };

 injection.processInjection(field, instance, mocks);
 assertFalse(mocks.contains(mockCandidate));
 verify(mockCandidate).thenInject(instance);
 }

 @Test
 public void testProcessInjection_mockCandidateFoundByName() {
 Class<?> awaitingInjectionClazz = mock(Class.class);
 Object mockCandidate1 = mock(Field.class);
 Object mockCandidate2 = mock(Field.class);
 Set<Object> mocks = Sets.newMockSafeHashSet(mockCandidate1, mockCandidate2);
 Object instance = mock(Object.class);

 Field field1 = new Field() {
 @Override
 public FieldType getType() {
 return FieldType.MOCK;
 }

 @Override
 public Class<?> getType() {
 return awaitingInjectionClazz;
 }

 @Override
 public String getName() {
 return "field1";
 }
 };

 Field field2 = new Field() {
 @Override
 public FieldType getType() {
 return FieldType.MOCK;
 }

 @Override
 public Class<?> getType() {
 return awaitingInjectionClazz;
 }

 @Override
 public String getName() {
 return "field2";
 }
 };

 injection.processInjection(field1, instance, mocks);
 assertFalse(mocks.contains(mockCandidate2));
 verify(mockCandidate1).thenInject(instance);
 }

 @Test
 public void testInjectMockCandidates_noMockCandidatesFound() {
 Class<?> awaitingInjectionClazz = mock(Class.class);
 Set<Object> mocks = Sets.newMockSafeHashSet();
 Object instance = mock(Object.class);

 List<Field> orderedInstanceFields = new ArrayList<>();
 orderedInstanceFields.add(new Field() {
 @Override
 public FieldType getType() {
 return FieldType.MOCK;
 }

 @Override
 public Class<?> getType() {
 return awaitingInjectionClazz;
 }
 });

 assertFalse(injection.injectMockCandidates(awaitingInjectionClazz, mocks, instance));
 }

 @Test
 public void testInjectMockCandidates_mockCandidatesFound() {
 Class<?> awaitingInjectionClazz = mock(Class.class);
 Object mockCandidate = mock(Field.class);
 Set<Object> mocks = Sets.newMockSafeHashSet(mockCandidate);
 Object instance = mock(Object.class);

 Field field = new Field() {
 @Override
 public FieldType getType() {
 return FieldType.MOCK;
 }

 @Override
 public Class<?> getType() {
 return awaitingInjectionClazz;
 }
 };

 List<Field> orderedInstanceFields = new ArrayList<>();
 orderedInstanceFields.add(field);

 assertFalse(injection.injectMockCandidates(awaitingInjectionClazz, mocks, instance));
 verifyNoInteractions(mockCandidate);
 }

 @Test
 public void testInjectMockCandidates_mockCandidatesInjected() {
 Class<?> awaitingInjectionClazz = mock(Class.class);
 Object mockCandidate = mock(Field.class);
 Set<Object> mocks = Sets.newMockSafeHashSet(mockCandidate);
 Object instance = mock(Object.class);

 Field field = new Field() {
 @Override
 public FieldType getType() {
 return FieldType.MOCK;
 }

 @Override
 public Class<?> getType() {
 return awaitingInjectionClazz;
 }
 };

 List<Field> orderedInstanceFields = new ArrayList<>();
 orderedInstanceFields.add(field);

 injection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);
 assertFalse(mocks.contains(mockCandidate));
 verify(mockCandidate).thenInject(instance);
 }

 @Test
 public void testOrderedInstanceFieldsFrom_emptyList() {
 Class<?> awaitingInjectionClazz = mock(Class.class);
 List<Field> orderedInstanceFields = injection.orderedInstanceFieldsFrom(awaitingInjectionClazz);
 assertTrue(orderedInstanceFields.isEmpty());
 }

 @Test
 public void testOrderedInstanceFieldsFrom_singleField() {
 Class<?> awaitingInjectionClazz = mock(Class.class);
 Field field = new Field() {
 @Override
 public FieldType getType() {
 return FieldType.MOCK;
 }

 @Override
 public Class<?> getType() {
 return awaitingInjectionClazz;
 }
 };
 List<Field> orderedInstanceFields = injection.orderedInstanceFieldsFrom(awaitingInjectionClazz);
 assertEquals(1, orderedInstanceFields.size());
 assertEquals(field, orderedInstanceFields.get(0));
 }

 @Test
 public void testOrderedInstanceFieldsFrom_multipleFields() {
 Class<?> awaitingInjectionClazz = mock(Class.class);
 Field field1 = new Field() {
 @Override
 public FieldType getType() {
 return FieldType.MOCK;
 }

 @Override
 public Class<?> getType() {
 return awaitingInjectionClazz;
 }

 @Override
 public String getName() {
 return "field1";
 }
 };
 Field field2 = new Field() {
 @Override
 public FieldType getType() {
 return FieldType.MOCK;
 }

 @Override
 public Class<?> getType() {
 return awaitingInjectionClazz;
 }

 @Override
 public String getName() {
 return "field2";
 }
 };
 List<Field> orderedInstanceFields = injection.orderedInstanceFieldsFrom(awaitingInjectionClazz);
 assertEquals(2, orderedInstanceFields.size());
 assertFalse(orderedInstanceFields.get(0).equals(field2));
 assertFalse(orderedInstanceFields.get(1).equals(field1));
 }
}