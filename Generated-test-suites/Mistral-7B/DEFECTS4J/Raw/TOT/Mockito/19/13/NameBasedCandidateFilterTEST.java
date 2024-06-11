package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private MockUtil mockUtil;

 @Before
 public void setUp() {
 mockUtil = Mockito.spy(new MockUtil());
 filter = new NameBasedCandidateFilter(null);
 }

 @Test
 public void testFilterCandidate_OneMockWithMatchingName() {
 List<Object> mocks = new ArrayList<>();
 mocks.add(mock(SomeClass.class, "mock1"));

 Field field = MockUtil.class.getDeclaredField("someField");
 Object fieldInstance = new Object();

 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

 assertEquals("mock1", injecter.getMock().getClass().getName());
 }

 @Test
 public void testFilterCandidate_MultipleMocksWithMatchingNames() {
 List<Object> mocks = new ArrayList<>();
 mocks.add(mock(SomeClass.class, "mock1"));
 mocks.add(mock(SomeClass.class, "mock1"));

 Field field = MockUtil.class.getDeclaredField("someField");
 Object fieldInstance = new Object();

 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

 // Since the filter checks field names against mock names, it should pick the first mock
 assertEquals("mock1", injecter.getMock().getClass().getName());
 }

 @Test
 public void testFilterCandidate_NoMatchingMocks() {
 List<Object> mocks = new ArrayList<>();
 mocks.add(mock(SomeOtherClass.class, "mock1"));

 Field field = MockUtil.class.getDeclaredField("someField");
 Object fieldInstance = new Object();

 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

 assertEquals(null, injecter.getMock());
 }

 @Test
 public void testFilterCandidate_NullMockCollection() {
 Field field = MockUtil.class.getDeclaredField("someField");
 Object fieldInstance = new Object();

 OngoingInjecter injecter = filter.filterCandidate(null, field, fieldInstance);

 assertEquals(null, injecter.getMock());
 }
}