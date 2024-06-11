import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {
    private NameBasedCandidateFilter filter;

    @Before
    public void setUp() {
        filter = new NameBasedCandidateFilter(null);
    }

    @Test
    public void filterCandidate_SingleMockWithMatchingName_ReturnsMatchingMock() throws NoSuchFieldException {
        // Arrange
        List<Object> mocks = new ArrayList<>();
        TestMock testMock = new TestMock();
        testMock.setName("mock");
        mocks.add(testMock);

        Field field = TestClass.class.getField("field");
        Object fieldInstance = null;

        // Act
        NameBasedCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        Assert.assertTrue(result.getMocks().contains(testMock));
    }

    @Test
    public void filterCandidate_SingleMockWithNonMatchingName_ReturnsEmpty() throws NoSuchFieldException {
        // Arrange
        List<Object> mocks = new ArrayList<>();
        TestMock testMock = new TestMock();
        testMock.setName("notMock");
        mocks.add(testMock);

        Field field = TestClass.class.getField("field");
        Object fieldInstance = null;

        // Act
        NameBasedCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        Assert.assertTrue(result.getMocks().isEmpty());
    }

    @Test
    public void filterCandidate_MultipleMocksWithMatchingName_ReturnsMatchingMocks() throws NoSuchFieldException {
        // Arrange
        List<Object> mocks = new ArrayList<>();
        TestMock testMock1 = new TestMock();
        testMock1.setName("mock");
        TestMock testMock2 = new TestMock();
        testMock2.setName("mock");
        TestMock testMock3 = new TestMock();
        testMock3.setName("notMock");
        mocks.add(testMock1);
        mocks.add(testMock2);
        mocks.add(testMock3);

        Field field = TestClass.class.getField("field");
        Object fieldInstance = null;

        // Act
        NameBasedCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        Assert.assertTrue(result.getMocks().contains(testMock1));
        Assert.assertTrue(result.getMocks().contains(testMock2));
        Assert.assertFalse(result.getMocks().contains(testMock3));
    }

    @Test
    public void filterCandidate_NoMocks_ReturnsEmpty() throws NoSuchFieldException {
        // Arrange
        List<Object> mocks = new ArrayList<>();
        Field field = TestClass.class.getField("field");
        Object fieldInstance = null;

        // Act
        NameBasedCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        Assert.assertTrue(result.getMocks().isEmpty());
    }

    @Test
    public void filterCandidate_MultipleMocksWithNoMatchingName_ReturnsAllMocks() throws NoSuchFieldException {
        // Arrange
        List<Object> mocks = new ArrayList<>();
        TestMock testMock1 = new TestMock();
        testMock1.setName("notMock");
        TestMock testMock2 = new TestMock();
        testMock2.setName("notMock");
        TestMock testMock3 = new TestMock();
        testMock3.setName("notMock");
        mocks.add(testMock1);
        mocks.add(testMock2);
        mocks.add(testMock3);

        Field field = TestClass.class.getField("field");
        Object fieldInstance = null;

        // Act
        NameBasedCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        Assert.assertTrue(result.getMocks().contains(testMock1));
        Assert.assertTrue(result.getMocks().contains(testMock2));
        Assert.assertTrue(result.getMocks().contains(testMock3));
    }

    class TestMock {
        private String name;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    class TestClass {
        public TestMock field;
    }
}