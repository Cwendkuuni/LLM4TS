package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.exceptions.Reporter;
import java.lang.reflect.Field;
import java.util.Collection;

public class FinalMockCandidateFilterTest {
    @Test
    public void testFilterCandidate() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // Test case 1: mocks size is 1
        Collection<Object> mocks1 = new Collection<>();
        mocks1.add(new Object());

        Field field1 = new Field();
        Object fieldInstance1 = new Object();

        OngoingInjecter result1 = filter.filterCandidate(mocks1, field1, fieldInstance1);
        assertNotNull(result1);
        
        Object actualResult1 = result1.thenInject();
        assertNotNull(actualResult1);
        
        BeanPropertySetter propertySetter1 = new BeanPropertySetter(fieldInstance1, field1);
        FieldSetter fieldSetter1 = new FieldSetter(fieldInstance1, field1);

        try {
            assertTrue(propertySetter1.set(actualResult1));
            assertFalse(fieldSetter1.set(actualResult1));
        } catch (RuntimeException e) {
            new Reporter().cannotInjectDependency(field1, actualResult1, e);
        }
        
        // Test case 2: mocks size is not 1
        Collection<Object> mocks2 = new Collection<>();
        mocks2.add(new Object());
        mocks2.add(new Object());
        
        Field field2 = new Field();
        Object fieldInstance2 = new Object();
        
        OngoingInjecter result2 = filter.filterCandidate(mocks2, field2, fieldInstance2);
        assertNotNull(result2);
        
        Object actualResult2 = result2.thenInject();
        assertNull(actualResult2);
    }
}