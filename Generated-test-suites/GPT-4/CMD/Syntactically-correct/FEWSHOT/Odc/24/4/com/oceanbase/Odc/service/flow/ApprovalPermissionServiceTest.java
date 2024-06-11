
package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.model.User;

public class ApprovalPermissionServiceTest {

    @InjectMocks
    private ApprovalPermissionService service;

    @Mock
    private UserService userService;

    @Mock
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getCandidates() {
        Long approvalInstanceId = 1L;
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        UserTaskInstanceCandidateEntity entity = new UserTaskInstanceCandidateEntity();
        entity.setUserId(1L);

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(Collections.singletonList(entity));
        when(userService.getUserById(1L)).thenReturn(userEntity);

        Set<User> result = service.getCandidates(approvalInstanceId, true);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    // Similar way we can write Junit test for remaining methods
}
