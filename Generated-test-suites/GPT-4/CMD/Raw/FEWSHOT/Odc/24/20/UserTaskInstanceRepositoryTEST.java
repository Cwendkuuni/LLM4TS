
package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class UserTaskInstanceRepositoryTest {
      
    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;
     
    private UserTaskInstanceEntity instanceEntity;
    private Long existingId;
    private FlowNodeStatus existingStatus;
    private List<Long> roleIdsList;
    private List<String> resourceRoleIdsList;
    
    // Setup initial data for each test
    @Before
    public void setup() {
        existingId = 1L; // Assuming an entity exists with id 1. Adjust according to your test data in test DB.
        existingStatus = FlowNodeStatus.IN_PROGRESS; // Assuming existing status is 'IN_PROGRESS'. Adjust according to your test data in test DB.
        roleIdsList = Arrays.asList(1L, 2L, 3L); // Sample roleIds list. Adjust according to your test data in test DB.
        resourceRoleIdsList = Arrays.asList("resourceId1", "resourceId2", "resourceId3"); // Sample resourceRoleIds list. Adjust according to your test data in test DB.
        //Similarly, setup instanceEntity with the data to be used for updating. 
    }
     
    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(existingStatus);
        assertNotNull(entities);
        assertFalse(entities.isEmpty());
    }
     
    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(existingId, roleIdsList);
        assertNotNull(entities);
        assertFalse(entities.isEmpty());
    }
     
    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(existingId, resourceRoleIdsList);
        assertNotNull(entities);
        assertFalse(entities.isEmpty());
    }
    
    //Similar tests to be created for all remaining methods
}
