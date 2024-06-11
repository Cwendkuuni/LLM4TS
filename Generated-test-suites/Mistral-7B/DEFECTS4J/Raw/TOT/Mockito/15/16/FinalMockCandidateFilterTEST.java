import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldType;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_OneMockAndFieldSet_Success() {
        // Given
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(FieldType.class);
        Object mock = mock(ClassWithField.class);
        Collection<Object> mocks = Arrays.asList(mock);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(mock, injecter.getMock());
    }
}

class ClassWithField {
    private SomeField someField;

    public SomeField getSomeField() {
        return someField;
    }

    public void setSomeField(SomeField someField) {
        this.someField = someField;
    }
}

class SomeField {
}