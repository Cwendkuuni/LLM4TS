import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.*;
import org.mockito.mock.MockHandlerInterface;

import static org.mockito.Mockito.RETURNS_DEFAULTS;
import static org.mockito.Mockito.withSettings;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        
        Assert.assertNotNull(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockObject() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(new Object());
    }

    @Test
    public void testIsMockWithMockObject() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        
        boolean isMock = mockUtil.isMock(mock);
        
        Assert.assertTrue(isMock);
    }

    @Test
    public void testIsMockWithNonMockObject() {
        MockUtil mockUtil = new MockUtil();
        boolean isMock = mockUtil.isMock(new Object());
        
        Assert.assertFalse(isMock);
    }
    
    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        
        mockUtil.resetMock(mock);
    }

    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        
        MockName mockName = mockUtil.getMockName(mock);
        
        Assert.assertNotNull(mockName);
    }
}

