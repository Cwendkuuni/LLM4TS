import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
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
import java.util.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class PropertyAndSetterInjectionTest {

 private PropertyAndSetterInjection injection;
 private Field injectMocksField;
 private Object injectMocksFieldOwner;
 private Set<Object> mockCandidates;

 @Before
 public void setup() {
 injection = new PropertyAndSetterInjection();
 injectMocksField = Mockito.spy(new Field() {
 @Override
 public Class<?> getType() {
 return String.class;
 }

 @Override
 public String getName() {
 return "injectMocksField";
 }

 @Override
 public int getModifiers() {
 return Modifier.PROTECTED;
 }
 });
 injectMocksFieldOwner = Mockito.mock(Object.class);
 mockCandidates = new HashSet<>();
 mockCandidates.add(mock(String.class));
 }

 @Test
 public void testProcessInjection_InitializesField() {
 FieldInitializationReport report = injection.initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);
 assertNotNull(report);
 }

 @Test(expected = MockitoException.class)
 public void testProcessInjection_ThrowsExceptionWhenInitializationFails() {
 FieldInitializationReport report = injection.initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);
 Mockito.doThrow(new RuntimeException("Test Exception")).when(injectMocksField).initialize();
 injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
 }

 @Test
 public void testProcessInjection_InjectionOccurs() {
 boolean injectionOccurred = injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
 assertFalse(injectionOccurred);
 }

 @Test
 public void testProcessInjection_MockIsInjectedViaSetter() throws IllegalAccessException {
 Object injectedMock = mock(String.class);
 injectMocksField.setAccessible(true);
 injectMocksField.set(injectMocksFieldOwner, injectedMock);
 mockCandidates.add(injectedMock);
 injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
 assertFalse(mockCandidates.contains(injectedMock));
 }

 @Test
 public void testProcessInjection_MockIsInjectedViaField() throws IllegalAccessException {
 Object injectedMock = mock(String.class);
 Field field = Mockito.spy(new Field() {
 @Override
 public Class<?> getType() {
 return String.class;
 }

 @Override
 public String getName() {
 return "injectedField";
 }

 @Override
 public int getModifiers() {
 return Modifier.PRIVATE;
 }
 });
 field.setAccessible(true);
 injectMocksFieldOwner.getClass().getDeclaredField("injectedField").setAccessible(true);
 injectMocksFieldOwner.getClass().getDeclaredField("injectedField").set(injectMocksFieldOwner, injectedMock);
 field.set(injectMocksFieldOwner, injectedMock);
 mockCandidates.add(injectedMock);
 injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
 assertFalse(mockCandidates.contains(injectedMock));
 }

 @Test
 public void testInjectMockCandidates_NoMocksInjected() {
 Class<?> awaitingInjectionClazz = String.class;
 boolean injectionOccurred = injection.injectMockCandidates(awaitingInjectionClazz, mockCandidates, injectMocksFieldOwner);
 assertFalse(injectionOccurred);
 }

 @Test
 public void testInjectMockCandidates_MockIsInjectedViaType() {
 Set<Object> mocks = new HashSet<>();
 mocks.add(mock(String.class));
 Class<?> awaitingInjectionClazz = String.class;
 boolean injectionOccurred = injection.injectMockCandidates(awaitingInjectionClazz, mocks, injectMocksFieldOwner);
 assertFalse(injectionOccurred);
 assertFalse(mockCandidates.contains(mocks.iterator().next()));
 }

 @Test
 public void testInjectMockCandidates_MockIsInjectedViaName() {
 Set<Object> mocks = new HashSet<>();
 mocks.add(mock(String.class));
 mocks.add(mock(Long.class)); // Different type, same name
 Class<?> awaitingInjectionClazz = String.class;
 boolean injectionOccurred = injection.injectMockCandidates(awaitingInjectionClazz, mocks, injectMocksFieldOwner);
 assertFalse(injectionOccurred);
 assertFalse(mockCandidates.contains(mocks.iterator().next()));
 }

 @Test
 public void testProcessInjection_MultipleFieldsWithSameName() {
 Class<?> awaitingInjectionClazz = new Object() {
 Field field1 = new Field() {
 @Override
 public Class<?> getType() {
 return String.class;
 }

 @Override
 public String getName() {
 return "injectedField";
 }

 @Override
 public int getModifiers() {
 return Modifier.PRIVATE;
 }
 };

 Field field2 = new Field() {
 @Override
 public Class<?> getType() {
 return String.class;
 }

 @Override
 public String getName() {
 return "injectedField";
 }

 @Override
 public int getModifiers() {
 return Modifier.PRIVATE;
 }
 };
 }.getClass();
 Object injectMocksFieldOwner = Mockito.mock(Object.class);
 Set<Object> mockCandidates = new HashSet<>();
 mockCandidates.add(mock(String.class));
 boolean injectionOccurred = injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
 assertFalse(injectionOccurred);
 assertFalse(mockCandidates.contains(mockCandidates.iterator().next()));
 }
}