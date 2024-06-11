
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

  private FinalMockCandidateFilter mockCandidateFilter;
  private Reporter reporter;

  @Before
  public void setup() {
    this.mockCandidateFilter = new FinalMockCandidateFilter();
    this.reporter = spy(new Reporter());
  }

  @Test
  public void whenSingleMockInCollection_thenSingleMatchingMockReturned() throws Exception {
    // Arrange
    Collection<Object> mocks = Arrays.asList(mock(Object.class));
    Field fieldMock = MyClass.class.getDeclaredField("myField");
    MyClass myFieldInstance = new MyClass();

    // Act
    OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldMock, myFieldInstance);

    // Assert
    assertTrue(result instanceof OngoingInjecter);
    assertNotNull(result.thenInject());
    verify(reporter, never()).cannotInjectDependency(any(Field.class), any(Object.class), any(RuntimeException.class));
  }

  @Test
  public void whenMultipleMocksInCollection_thenNullReturned() throws Exception {
    // Arrange
    Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
    Field fieldMock = MyClass.class.getDeclaredField("myField");
    MyClass myFieldInstance = new MyClass();

    // Act
    OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldMock, myFieldInstance);

    // Assert
    assertTrue(result instanceof OngoingInjecter);
    assertNull(result.thenInject());
    verify(reporter, never()).cannotInjectDependency(any(Field.class), any(Object.class), any(RuntimeException.class));
  }

  @Test
  public void whenSettingFieldFails_thenReporterCalled() throws Exception {
    // Arrange
    Collection<Object> mocks = Arrays.asList(mock(Object.class));
    Field fieldMock = MyClass.class.getDeclaredField("myField");
    MyClass myFieldInstance = new MyClass();
    BeanPropertySetter setterMock = mock(BeanPropertySetter.class);
    doReturn(false).when(setterMock).set(any(Object.class));

    // Act
    OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldMock, myFieldInstance);

    // Assert
    assertTrue(result instanceof OngoingInjecter);
    assertNotNull(result.thenInject());
    verify(reporter, times(1)).cannotInjectDependency(eq(fieldMock), any(Object.class), any(RuntimeException.class));
  }

  @Test(expected = NoSuchFieldException.class)
  public void givenInvalidField_whenFilterCandidate_thenNoSuchFieldException() throws Exception {
    // Arrange
    Collection<Object> mocks = Arrays.asList(mock(Object.class));
    Field invalidFieldMock = MyClass.class.getDeclaredField("invalidField");
    MyClass myFieldInstance = new MyClass();

    // Act
    mockCandidateFilter.filterCandidate(mocks, invalidFieldMock, myFieldInstance);

    // Assert
    // Expect NoSuchFieldException
  }
}

class MyClass {
  private Object myField;
}
