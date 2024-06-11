import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {
  
  @InjectMocks
  private RoleRepository roleRepository;
  
  @Mock
  private RoleEntity roleEntity;
  
  @Before
  public void setUp() {
    roleEntity = new RoleEntity();
    roleEntity.setId(1L);
    roleEntity.setName("Test Role");
    roleEntity.setEnabled(true);
    roleEntity.setDescription("Test Role Description");
  }
  
  @Test
  public void testFindByIdIn() {
    List<RoleEntity> roleEntities = new ArrayList<>();
    roleEntities.add(roleEntity);
    
    List<RoleEntity> result = roleRepository.findByIdIn(Collections.singletonList(1L));
    
    assertEquals(roleEntities.size(), result.size());
    assertEquals(roleEntity, result.get(0));
  }
  
  @Test
  public void testFindByName() {
    List<RoleEntity> roleEntities = new ArrayList<>();
    roleEntities.add(roleEntity);
    
    List<RoleEntity> result = roleRepository.findByName("Test Role");
    
    assertEquals(roleEntities.size(), result.size());
    assertEquals(roleEntity, result.get(0));
  }
  
  // Additional test cases for other methods can be added here
  
}