package org.mockito.internal.configuration.injection;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class PropertyAndSetterInjectionTest {

    @Test
    public void testProcessInjection() throws Exception {
        PropertyAndSetterInjection strategy = new PropertyAndSetterInjection();

        // create mock candidates
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(new MockCandidate());
        mockCandidates.add(new AnotherMockCandidate());

        // create inject mocks field and owner
        Field injectMocksField = PropertyAndSetterInjection.class.getDeclaredField("mockCandidateFilter");
        Object injectMocksFieldOwner = strategy;

        // test processInjection method
        boolean injectionOccurred = strategy.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // assert injection occurred
        Assert.assertTrue(injectionOccurred);
    }

    private static class MockCandidate {
    }

    private static class AnotherMockCandidate {
    }
}