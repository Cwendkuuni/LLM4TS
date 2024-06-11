package com.oceanbase.odc.metadb.flow;
import org.junit.*;
import org.mockito.*;
import java.util.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ExampleTest {
    @Mock
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenUserIdGiven_shouldReturnUser() {
        User yourUser = new User();
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(yourUser));
        
        User returnedUser = userService.findById(any(Long.class));
        Assert.assertEquals(returnedUser , yourUser);
    }
}