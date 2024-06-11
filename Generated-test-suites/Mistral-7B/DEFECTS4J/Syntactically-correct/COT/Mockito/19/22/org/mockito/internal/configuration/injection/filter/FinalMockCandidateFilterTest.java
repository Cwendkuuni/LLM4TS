import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.ReflectionUtils;
import org.mockito.exceptions.Reporter;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class FinalMockCandidateFilterTest {

 private FinalMockCandidateFilter filter;
 private Object fieldInstance;
 private Field field;
 private List<Object> mocks;
 private Reporter reporter;

 @Before
 public void before() {
 fieldInstance = mock(Object.class);
 field = ReflectionUtils.getField(fieldInstance, "someField");
 mocks = Arrays.asList(mock(Object.class), mock(Object.class));
 reporter = spy(new Reporter());
 filter = new FinalMockCandidateFilter();
 }

 @Test
 public void testFilterCandidateWhenOneMockIsProvided() {
 Object mock = mocks.get(0);
 OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);
 OngoingInjecter expected = new OngoingInjecter() {
 @Override
 public Object thenInject() {
 BeanPropertySetter propertySetter = new BeanPropertySetter(fieldInstance, field);
 propertySetter.set(mock);
 return mock;
 }
 };
 assertEquals(expected, actual);
 }

 @Test(expected = RuntimeException.class)
 public void testFilterCandidateWhenOneMockFailsToInject() {
 OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);
 BeanPropertySetter propertySetter = new BeanPropertySetter(fieldInstance, field);
 propertySetter.setException(new RuntimeException("Some error message"));
 mocks.get(0).getClass().getDeclaredMethod("throwException").invoke(mocks.get(0));
 assertEquals(null, actual.thenInject());
 new Reporter().cannotInjectDependency(field, mocks.get(0), new RuntimeException("Some error message"));
 }

 @Test
 public void testFilterCandidateWhenMoreThanOneMockIsProvided() {
 OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);
 assertEquals(new OngoingInjecter() {
 @Override
 public Object thenInject() {
 return null;
 }
 }, actual);
 }
}