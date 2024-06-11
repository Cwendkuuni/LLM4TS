package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private ConnectionService connectionService;
    
    private OscService oscService;
    
    @Before
    public void setUp() {
        oscService = new OscService();
    }
    
    @Test
    public void testGetOscDatabaseInfo(){
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        Mockito.when(databaseRepository.findById(Matchers.anyLong())).thenReturn(Optional.of(databaseEntity));
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
        assertThat(result.getDatabaseId()).isEqualTo(databaseEntity.getDatabaseId());
    }
    
    @Test(expected = NoSuchElementException.class)
    public void testGetOscDatabaseInfo_NoSuchElement(){
        Mockito.when(databaseRepository.findById(Matchers.anyLong())).thenReturn(Optional.empty());
        oscService.getOscDatabaseInfo(1L);
    }
    
    @Test
    public void testSwapTable(){
        // Add code here with proper assertion and given scenarios to test swapTable method.
    }
    
    @Test(expected = NoSuchElementException.class)
    public void testSwapTable_NoSuchElement(){
        // Add code here to test swapTable method exception with a condition under which NoSuchElementException occurs.
    }

    @Test
    public void testGetLockUserIsRequired() {
        /* Similar to above methods, generate appropriate scenario and assertor to test getLockUserIsRequired private method,
           Though normally private methods are not tested as they are used internally in the class, 
           in some critical scenarios they might need to be tested.*/
    }
}
