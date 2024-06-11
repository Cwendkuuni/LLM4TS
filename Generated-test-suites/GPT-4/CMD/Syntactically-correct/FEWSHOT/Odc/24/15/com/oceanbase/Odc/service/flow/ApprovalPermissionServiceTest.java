package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ApprovalPermissionServiceTest {

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Mock
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    public void getCandidatesTest() {
        UserTaskInstanceCandidateEntity entity = new UserTaskInstanceCandidateEntity();
        entity.setUserId(1L);
        entity.setRoleId(1L);

        User user = new User();
        user.setId(entity.getUserId());
        user.setName("Jane Doe");

        Set<User> expected = new HashSet<>();
        expected.add(user);

        when(userTaskCandidateRepository.findByApprovalInstanceId(1L))
                .thenReturn(Collections.singletonList(entity));
        when(userRepository.findByUserIdsAndEnabled(Collections.singleton(entity.getUserId()), true))
                .thenReturn(Collections.singletonList(user));

        Set<User> actual = approvalPermissionService.getCandidates(1L, true);

        assertEquals(expected, actual);
    }
    
    //Continue to create tests for each remaining method using a similar pattern.
}