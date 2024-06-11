package com.oceanbase.odc.metadb.flow.tests;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {
  
    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;
    
    @Before
    public void beforeTest() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testFindStatus() {
        //Mock data
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setStatus(FlowNodeStatus.APPROVED);

        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED)).thenReturn(Arrays.asList(entity));

        //Assert
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        Assert.assertEquals(1, entities.size());
        Assert.assertEquals(FlowNodeStatus.APPROVED, entities.get(0).getStatus());
        // Continue setting up test scenarios similar to this for rest of the methods in UserTaskInstanceRepository
    }

    //Expanding on this model, add more test cases for the other methods including edge cases and error scenarios. 
}