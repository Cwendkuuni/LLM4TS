
package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TypeBasedCandidateFilterTest {

    @Mock
    private MockCandidateFilter nextMock;

    private TypeBasedCandidateFilter candidateFilter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        candidateFilter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_shouldReturnOnlyMatches() {
        // Arrange
        Field field = getField();
        List<Object> mocks = getMockList();
        List<Object> expectedMatches = getExpectedMatches();

        // Act
        candidateFilter.filterCandidate(mocks, field, null);

        // Assert
        verify(nextMock, times(1)).filterCandidate(eq(expectedMatches), eq(field), isNull());
    }

    private Field getField() {
        try {
            return MyClass.class.getField("myField");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<Object> getMockList() {
        List<Object> mocks = new ArrayList<>();
        mocks.add(new MyFirstMock());
        mocks.add(new MySecondMock());
        mocks.add(new MyThirdMock());

        return mocks;
    }

    private List<Object> getExpectedMatches() {
        List<Object> expectedMatches = new ArrayList<>();
        expectedMatches.add(new MyFirstMock());
        expectedMatches.add(new MySecondMock());

        return expectedMatches;
    }
}

class MyClass {
    public MyFirstMock myField;
}

class MyFirstMock {

}

class MySecondMock {

}

class MyThirdMock {

}
