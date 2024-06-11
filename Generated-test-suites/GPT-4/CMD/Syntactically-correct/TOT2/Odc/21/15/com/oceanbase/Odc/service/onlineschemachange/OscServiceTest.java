package com.oceanbase.odc.service.onlineschemachange;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;

public class OscServiceTest {

    @Autowired
    private OscService oscService;
    private DatabaseRepository mockDatabaseRepository;
    private ConnectionService mockConnectionService;
    private ScheduleTaskRepository mockScheduleTaskRepository;
    private ScheduleRepository mockScheduleRepository;

    @Before
    public void setUp() {
        mockDatabaseRepository = mock(DatabaseRepository.class);
        mockConnectionService = mock(ConnectionService.class);
        mockScheduleTaskRepository = mock(ScheduleTaskRepository.class);
        mockScheduleRepository = mock(ScheduleRepository.class);
        
        oscService = new OscService(mockDatabaseRepository, mockConnectionService, 
        mockScheduleTaskRepository, mockScheduleRepository);
    }
    
    @Test
    public void getOscDatabaseInfoTest() {
        Long id = 999L;
        Optional<DatabaseEntity> optionalDatabaseEntity = Optional.of(new DatabaseEntity());

        when(mockDatabaseRepository.findById(id)).thenReturn(optionalDatabaseEntity);
        
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        
        assertEquals(id, result.getDatabaseId());
        assertNotNull(result.getLockDatabaseUserRequired());
    }
    
    @Test
    public void getOscDatabaseInfoNoDatabaseTest() {
        Long id = 999L;
        Optional<DatabaseEntity> optionalDatabaseEntity = Optional.empty();

        when(mockDatabaseRepository.findById(id)).thenReturn(optionalDatabaseEntity);
        
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        
        assertNull(result.getDatabaseId());
        assertNotNull(result.getLockDatabaseUserRequired());
    }
    
    @Test
    public void swapTableTest() {
        Long id = 999L;
        Optional<ScheduleTaskEntity> optionalScheduleTaskEntity = Optional.of(new ScheduleTaskEntity());
        ScheduleEntity scheduleEntity = getScheduleEntity();

        when(mockScheduleTaskRepository.findById(id)).thenReturn(optionalScheduleTaskEntity);
        when(mockScheduleRepository.findById(scheduleId)).thenReturn(Optional.of(scheduleEntity));
        
        OscSwapTableVO result = oscService.swapTable(id);
        
        assertNotNull(result);
        assertEquals(id, result.getScheduleTaskId());
    }
    
    @Test(expected = IllegalArgumentException.class) 
    public void swapTableExceptionTest() {
        Long id = 999L;
        Optional<ScheduleTaskEntity> optionalScheduleTaskEntity = Optional.empty();
    
        when(mockScheduleTaskRepository.findById(id)).thenReturn(optionalScheduleTaskEntity);
        oscService.swapTable(id);
    }
}
