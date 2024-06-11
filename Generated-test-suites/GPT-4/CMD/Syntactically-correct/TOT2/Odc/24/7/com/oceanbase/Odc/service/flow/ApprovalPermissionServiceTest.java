package com.oceanbase.odc.service.flow;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Set;
import java.util.Arrays;

import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

public class ApprovalPermissionServiceTest {

    @InjectMocks
    ApprovalPermissionService approvalPermissionService;

    @Mock
    UserService userServiceMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    /*
     * Test the getCandidates method with typical use case 
     */
    @Test
    public void getCandidatesTest_typicalUseCase() {
        Long approvalInstanceId = 1L;
        boolean enabled = true;

        Set<User> mockCandidates = new HashSet<>(Arrays.asList(
            new User(1L, "TestUser1"),
            new User(2L, "TestUser2")
        ));

        when(userServiceMock.findByRoleIdsAndEnabled(any(), anyBoolean()))
            .thenReturn(mockCandidates);

        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        assertEquals(mockCandidates, candidates);
    }

    /*
     * Test getCandidates method with edge case (add test case description)
     */
    @Test
    public void getCandidatesTest_edgeCase() {
        //To-Do implement the edge case 
    }

    /*
     * Test getCandidates method with error case (add test case description)
     */
    @Test
    public void getCandidatesTest_errorCase() {
        //To-Do implement the error case
    }

}
