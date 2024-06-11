package fr.unice.gfarce.dao;

import javax.persistence.*;
import java.util.*;
import fr.unice.gfarce.identity.*;
import org.junit.*;
import static org.junit.Assert.*;

public class OracleIdentiteDaoTest {
  
  @Test
  public void testInsert() {
    OracleIdentiteDao dao = new OracleIdentiteDao();
    Identite identite = new Identite();
    String nomForm = "Nom";
    Calendar dateLimite = Calendar.getInstance();
    
    dao.insert(identite, nomForm, dateLimite);
    
    // Assert statements
    // ...
  }
  
  @Test
  public void testUpdate() {
    OracleIdentiteDao dao = new OracleIdentiteDao();
    Identite identite = new Identite();
    
    dao.update(identite);
    
    // Assert statements
    // ...
  }
  
  @Test
  public void testDelete() {
    OracleIdentiteDao dao = new OracleIdentiteDao();
    Identite identite = new Identite();
    
    dao.delete(identite);
    
    // Assert statements
    // ...
  }
  
  @Test
  public void testFind() {
    OracleIdentiteDao dao = new OracleIdentiteDao();
    String nom = "Nom";
    String prenom = "Prenom";
    String sex = "Sex";
    String email = "Email";
    Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
    
    List<Identite> result = dao.find(nom, prenom, sex, email, type);
    
    // Assert statements
    // ...
  }
  
  @Test
  public void testFindCandidat() {
    OracleIdentiteDao dao = new OracleIdentiteDao();
    String nom = "Nom";
    String prenom = "Prenom";
    String sex = "Sex";
    String email = "Email";
    
    Candidat[] result = dao.findCandidat(nom, prenom, sex, email);
    
    // Assert statements
    // ...
  }
  
  @Test
  public void testFindRespForm() {
    OracleIdentiteDao dao = new OracleIdentiteDao();
    String nom = "Nom";
    String prenom = "Prenom";
    String sex = "Sex";
    String email = "Email";
    
    RespFormation[] result = dao.findRespForm(nom, prenom, sex, email);
    
    // Assert statements
    // ...
  }
  
}