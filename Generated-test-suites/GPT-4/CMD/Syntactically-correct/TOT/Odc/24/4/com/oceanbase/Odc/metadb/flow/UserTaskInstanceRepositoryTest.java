package com.oceanbase.odc.metadb.flow;
// Import necessary libraries
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

// Your test class
public class UserTaskInstanceRepositoryTest {
    
    @InjectMocks
    UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    EntityManager entityManager;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByStatus() {
        // Prepare test data

        // Call method and get the result

        // Assert result
    }

    // Similar skeleton would be followed for the rest of the public methods in your Interface
}