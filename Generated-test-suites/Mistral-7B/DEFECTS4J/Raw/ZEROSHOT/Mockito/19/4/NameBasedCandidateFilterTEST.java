package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.inject.OngoingStubbing;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.support.annotation.MockStatic;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@PrepareForTest(NameBasedCandidateFilter.class)
@MockStatic(MockUtil.class)
public class NameBasedCandidateFilterTest {

 private static final String MOCK_NAME = "mockName";
 private static final String FIELD_NAME = "fieldName";
 private static final String ANOTHER_FIELD_NAME = "anotherFieldName";
 private static final String FULL_FIELD_NAME = "fullFieldName";
 private static final String FIELD_TYPE = "java.lang.String";
 private NameBasedCandidateFilter filter;
 private MockCandidateFilter nextMock;
 private Object fieldInstance;
 private Collection<Object> mocks;

 @Before
 public void setUp() {
 fieldInstance = mock(Object.class);
 nextMock = mock(MockCandidateFilter.class);
 mocks = Arrays.asList(mock(Object.class), mock(Object.class));
 filter = new NameBasedCandidateFilter(nextMock);
 }

 @Test
 public void testFilterCandidateWhenMultipleMocksWithSameName() {
 // Given
 when(MockUtil.getMockName(mocks.get(0))).thenReturn(MOCK_NAME);
 when(MockUtil.getMockName(mocks.get(1))).thenReturn(MOCK_NAME);

 // When
 OngoingStubbing ongoingStubbing = filter.filterCandidate(mocks,
 new Field(this, FIELD_NAME), fieldInstance);

 // Then
 verify(nextMock).filterCandidate(mocks, new Field(this, FIELD_NAME),
 fieldInstance);
 assertEquals(mocks.get(0), ongoingStubbing.getMock());
 }

 @Test
 public void testFilterCandidateWhenMultipleFieldsWithSameName() {
 // Given
 Object testObject = spy(this);
 Class<?> clazz = getClass();
 Field field1 = new Field(testObject, FIELD_NAME);
 Field field2 = new Field(testObject, FIELD_NAME);
 when(MockUtil.getMockName(mocks.get(0))).thenReturn(MOCK_NAME);
 when(MockUtil.getMockName(mocks.get(1))).thenReturn(MOCK_NAME);

 // When
 OngoingStubbing ongoingStubbing = filter.filterCandidate(mocks, field1,
 fieldInstance);

 // Then
 verify(nextMock).filterCandidate(mocks, field2, fieldInstance);
 assertEquals(mocks.get(0), ongoingStubbing.getMock());
 }

 @Test
 public void testFilterCandidateWhenNoConflictingFields() {
 // Given
 when(MockUtil.getMockName(mocks.get(0))).thenReturn(MOCK_NAME);

 // When
 OngoingStubbing ongoingStubbing = filter.filterCandidate(mocks,
 new Field(this, ANOTHER_FIELD_NAME), fieldInstance);

 // Then
 verify(nextMock).filterCandidate(mocks, new Field(this, ANOTHER_FIELD_NAME),
 fieldInstance);
 assertEquals(mocks.get(0), ongoingStubbing.getMock());
 }

 @Test
 public void testFilterCandidateWhenNoConflictingMocks() {
 // Given
 when(MockUtil.getMockName(mocks.get(0))).thenReturn(FULL_FIELD_NAME);
 when(MockUtil.getMockName(mocks.get(1))).thenReturn("anotherMockName");

 // When
 OngoingStubbing ongoingStubbing = filter.filterCandidate(mocks,
 new Field(this, FIELD_NAME), fieldInstance);

 // Then
 verify(nextMock).filterCandidate(mocks, new Field(this, FIELD_NAME),
 fieldInstance);
 assertEquals(mocks.get(0), ongoingStubbing.getMock());
 }
}