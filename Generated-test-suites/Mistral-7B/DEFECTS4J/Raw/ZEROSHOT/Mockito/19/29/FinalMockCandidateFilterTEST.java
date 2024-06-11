package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.stubs.MockitoStubs;

import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.reflection.ReflectionUtils.setFieldValue;
import static org.mockito.internal.util.reflection.ReflectionUtils.setPropertyValue;

public class FinalMockCandidateFilterTest {

 private FinalMockCandidateFilter filter;
 private Object mocksCollection;
 private Object mock1;
 private Object mock2;
 private Object fieldInstance;
 private Field field;

 @Before
 public void setUp() {
 mock1 = MockitoStubs.mock(String.class);
 mock2 = MockitoStubs.mock(String.class);
 mocksCollection = new Object() {
 @SuppressWarnings("unchecked")
 public Collection<Object> getMocks() {
 return (Collection<Object>) Arrays.asList(mock1, mock2);
 }
 };
 field = new Field("testField", String.class, Object.class);
 fieldInstance = new Object();
 try {
 FieldSetter.install(FieldSetter.class, fieldInstance, field);
 } catch (IllegalAccessException e) {
 throw new RuntimeException(e);
 }
 filter = new FinalMockCandidateFilter();
 }

 @Test
 public void testFilterCandidate_singleMock_propertySetter() {
 // given
 Object matchingMock = mock1;
 Object expected = mock1;

 // when
 OngoingInjecter injecter = filter.filterCandidate(mocksCollection, field, fieldInstance);
 injecter.thenInject();

 // then
 assertSame(expected, fieldInstance.getClass().getDeclaredField("testField").get(fieldInstance));
 }

 @Test
 public void testFilterCandidate_singleMock_fieldSetter() {
 // given
 Object matchingMock = mock1;
 Object expected = mock1;

 // when
 OngoingInjecter injecter = filter.filterCandidate(mocksCollection, field, fieldInstance);
 injecter.thenInject();

 // then
 assertSame(expected, fieldInstance);
 }

 @Test(expected = RuntimeException.class)
 public void testFilterCandidate_singleMock_exception() {
 // given
 Object matchingMock = mock1;
 Object expected = mock2;

 // when
 OngoingInjecter injecter = filter.filterCandidate(mocksCollection, field, fieldInstance);
 injecter.thenInject();

 // then
 assertSame(expected, fieldInstance.getClass().getDeclaredField("testField").get(fieldInstance)); // this line should throw an exception
 }

 @Test
 public void testFilterCandidate_multipleMocks_returnNullInjecter() {
 // given

 // when
 OngoingInjecter injecter = filter.filterCandidate(mocksCollection, field, fieldInstance);

 // then
 assertNull(injecter);
 }
}