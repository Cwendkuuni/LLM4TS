package org.mockito.internal.configuration.injection;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;
import java.lang.reflect.Field;
import java.util.Collection;

public class FinalMockCandidateFilterTest {

  @Test
  public void testFilterCandidate_singleMock() {
    Collection<Object> mocks = new ArrayList<>();
    Field field = null;
    Object fieldInstance = null;

    FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
    OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

    assertFalse(injecter.thenInject());
  }

  @Test
  public void testFilterCandidate_multipleMocks() {
    Collection<Object> mocks = new ArrayList<>();
    Object matchingMock = new Object();
    mocks.add(matchingMock);
    Field field = null;
    Object fieldInstance = null;

    FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
    OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

    assertTrue(injecter.thenInject());
  }

  @Test
  public void testFilterCandidate_exception() {
    Collection<Object> mocks = new ArrayList<>();
    Object matchingMock = new Object();
    mocks.add(matchingMock);
    Field field = null;
    Object fieldInstance = null;

    FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
    OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

    try {
      injecter.thenInject();
      fail("MockitoException not thrown");
    } catch (MockitoException e) {
      assertEquals("Problems injecting dependency in " + field.getName(), e.getMessage());
    }
  }

}