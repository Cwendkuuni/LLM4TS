package com.oceanbase.odc.metadb.flow;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> found = userTaskInstanceRepository.findByStatus(FlowNodeStatus.CREATED);
        assertNotNull(found);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> found = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, new ArrayList<>());
        assertNotNull(found);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> found = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, new ArrayList<>());
        assertNotNull(found);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> found = userTaskInstanceRepository.findByResourceRoleIdentifierIn(new ArrayList<>());
        assertNotNull(found);
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> found = userTaskInstanceRepository.findByRoleId(1L);
        assertNotNull(found);
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> found = userTaskInstanceRepository.findByCandidateUserId(1L);
        assertNotNull(found);
    }

    // Add more test methods for other repository methods

}