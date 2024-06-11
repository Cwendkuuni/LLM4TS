import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldType;
import org.mockito.internal.util.reflection.Invocable;
import org.mockito.internal.util.reflection.Reflections;
import org.powermock.api.mockito.internal.CreatedMockType;
import org.powermock.api.mockito.internal.util.CodeGenUtil;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Collection;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.internal.util.reflection.Reflections.getField;
import static org.mockito.internal.util.reflection.Reflections.getFieldValue;
import static org.mockito.internal.util.reflection.Reflections.setFieldValue;

public class FinalMockCandidateFilterTest {

 private FinalMockCandidateFilter filter;
 private Collection<Object> mocks;
 private Object fieldInstance;
 private Field field;
 private Object matchingMock;
 private Object nonMatchingMock;
 private Reporter reporter;
 private Invocable injector;

 @Before
 public void setUp() {
 mocks = Arrays.asList(new Object() {}, new Object() {}, new Object() {});
 filter = new FinalMockCandidateFilter();
 fieldInstance = new Object();
 field = Reflections.getField(fieldInstance, "field");
 matchingMock = new Object() {};
 nonMatchingMock = new Object() {};
 reporter = Reporter.MOCKITO_REPORTER;
 injector = new Invocable() {};
 }

 @Test
 public void testFilterCandidate_singleMock() {
 Object actual = filter.filterCandidate(mocks, field, fieldInstance);
 assertEquals(matchingMock, actual);
 }

 @Test(expected = RuntimeException.class)
 public void testFilterCandidate_singleMock_propertySetterFailure() {
 Whitebox.setInternalState(filter, "reporter", null);
 try {
 filter.filterCandidate(mocks, field, fieldInstance).thenInject();
 } catch (RuntimeException e) {
 throw e;
 }
 Whitebox.setInternalState(filter, "reporter", reporter);
 assertThrows(RuntimeException.class, () -> filter.filterCandidate(mocks, field, fieldInstance).thenInject());
 }

 @Test
 public void testFilterCandidate_singleMock_fieldAccess() {
 Whitebox.setInternalState(filter, "reporter", null);
 BeanPropertySetter beanPropertySetter = new BeanPropertySetter(fieldInstance, field);
 beanPropertySetter.set(matchingMock);
 Object actual = filter.filterCandidate(mocks, field, fieldInstance).thenInject();
 assertEquals(matchingMock, actual);
 Whitebox.setInternalState(filter, "reporter", reporter);
 }

 @Test
 public void testFilterCandidate_multipleMocks() {
 Object actual = filter.filterCandidate(mocks, field, fieldInstance).thenInject();
 assertNull(actual);
 }
}