package com.oceanbase.odc.service.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.model.User;

@RunWith(MockitoJUnitRunner.class)
public class ApprovalPermissionServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Test
    public void testGetCandidates() {
        UserTaskInstanceCandidateEntity entity = new UserTaskInstanceCandidateEntity();
        entity.setUserId(1L);
        when(userTaskCandidateRepository.findByApprovalInstanceId(1L)).thenReturn(Collections.singletonList(entity));

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        when(userRepository.findByUserIdsAndEnabled(anySet(), anyBoolean())).thenReturn(Collections.singletonList(userEntity));

        Set<User> result = approvalPermissionService.getCandidates(1L, true);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.stream().map(User::getId).collect(Collectors.toList()).contains(1L));
    }
    
    /* Add more tests here for other methods in ApprovalPermissionService */
}