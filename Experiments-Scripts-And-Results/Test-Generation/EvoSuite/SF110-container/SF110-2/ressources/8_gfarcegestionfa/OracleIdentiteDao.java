package fr.unice.gfarce.dao;

import javax.persistence.*;
import java.util.*;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDao implements IdentiteDao
{
    String persistUnitName;
    @PersistenceContext
    private EntityManager em;
    
    public OracleIdentiteDao() {
        this.persistUnitName = "Identite";
    }
    
    public static OracleIdentiteDao getDAO() {
        return new OracleIdentiteDao();
    }
    
    @Override
    public void insert(final Identite identite, final String nomForm, final Calendar dateLimite) {
        EntityManagerFactory emf = null;
        this.em = null;
        EntityTransaction tx = null;
        try {
            emf = Persistence.createEntityManagerFactory(this.persistUnitName);
            this.em = emf.createEntityManager();
            tx = this.em.getTransaction();
            tx.begin();
            this.em.persist((Object)identite);
            tx.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
        }
        finally {
            if (this.em != null) {
                this.em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }
    }
    
    @Override
    public void update(final Identite identite) {
        EntityManagerFactory emf = null;
        this.em = null;
        EntityTransaction tx = null;
        try {
            emf = Persistence.createEntityManagerFactory(this.persistUnitName);
            this.em = emf.createEntityManager();
            tx = this.em.getTransaction();
            tx.begin();
            final Identite i = (Identite)this.em.find((Class)identite.getClass(), (Object)identite.getId());
            i.setEmail(identite.getEmail());
            i.setNom(identite.getNom());
            i.setPrenom(identite.getPrenom());
            i.setSex(identite.getSex());
            i.setType(identite.getType());
            this.em.merge((Object)identite);
            tx.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
        }
        finally {
            if (this.em != null) {
                this.em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }
    }
    
    @Override
    public void delete(final Identite identite) {
        EntityManagerFactory emf = null;
        this.em = null;
        EntityTransaction tx = null;
        try {
            emf = Persistence.createEntityManagerFactory(this.persistUnitName);
            this.em = emf.createEntityManager();
            tx = this.em.getTransaction();
            tx.begin();
            final Identite ident = (Identite)this.em.find((Class)identite.getClass(), (Object)identite.getId());
            this.em.remove((Object)ident);
            tx.commit();
        }
        catch (Exception e1) {
            e1.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
        }
        finally {
            if (this.em != null) {
                this.em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }
    }
    
    public List<Identite> find(final String nom, final String prenom, final String sex, final String email, final Identite.TypeIdentite type) {
        String a;
        if (nom == null) {
            a = "ident.nom like '%'";
        }
        else {
            a = "ident.nom='" + nom + "'";
        }
        String b;
        if (prenom == null) {
            b = "ident.prenom like '%'";
        }
        else {
            b = "ident.prenom='" + prenom + "'";
        }
        String c;
        if (sex == null) {
            c = "ident.sex like '%'";
        }
        else {
            c = "ident.sex='" + sex + "'";
        }
        String e;
        if (type == Identite.TypeIdentite.CANDIDAT) {
            e = "ident.type= :CANDIDAT";
        }
        else if (type == Identite.TypeIdentite.FORMATEUR) {
            e = "ident.type= :FORMATEUR";
        }
        else {
            e = "ident.type like '%'";
        }
        final String s = "select ident from Identite as ident where " + a + " and " + b + " and " + c + " and " + e;
        EntityManagerFactory emf = null;
        this.em = null;
        EntityTransaction tx = null;
        List<Identite> list = null;
        try {
            emf = Persistence.createEntityManagerFactory(this.persistUnitName);
            this.em = emf.createEntityManager();
            tx = this.em.getTransaction();
            tx.begin();
            if (type == Identite.TypeIdentite.CANDIDAT) {
                list = (List<Identite>)this.em.createQuery(s).setParameter("CANDIDAT", (Object)Identite.TypeIdentite.CANDIDAT).getResultList();
            }
            else if (type == Identite.TypeIdentite.FORMATEUR) {
                list = (List<Identite>)this.em.createQuery(s).setParameter("FORMATEUR", (Object)Identite.TypeIdentite.FORMATEUR).getResultList();
            }
            else {
                list = (List<Identite>)this.em.createQuery(s).getResultList();
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
        }
        finally {
            if (this.em != null) {
                this.em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }
        return list;
    }
    
    @Override
    public Candidat[] findCandidat(final String nom, final String prenom, final String sex, final String email) {
        final List<Identite> resultList = this.find(nom, prenom, sex, email, Identite.TypeIdentite.CANDIDAT);
        if (resultList != null) {
            final Candidat[] result = resultList.toArray(new Candidat[resultList.size()]);
            return result;
        }
        return null;
    }
    
    @Override
    public RespFormation[] findRespForm(final String nom, final String prenom, final String sex, final String email) {
        final List<Identite> resultList = this.find(nom, prenom, sex, email, Identite.TypeIdentite.FORMATEUR);
        if (resultList != null) {
            final RespFormation[] result = resultList.toArray(new RespFormation[resultList.size()]);
            return result;
        }
        return null;
    }
}
