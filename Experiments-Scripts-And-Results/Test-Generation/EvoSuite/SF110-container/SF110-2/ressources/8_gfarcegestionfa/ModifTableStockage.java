package fr.unice.gfarce.interGraph;

import javax.swing.*;
import java.awt.*;

public class ModifTableStockage
{
    private int nbColonnes;
    private int nbLignes;
    private TableStockage ts;
    
    public ModifTableStockage(final TableStockage ts) {
        this.nbColonnes = 0;
        this.nbLignes = 0;
        this.nbColonnes = ts.getColumnCount();
        this.nbLignes = ts.getRowCount() + 1;
        this.ts = ts;
    }
    
    public TableStockage ajouterColonne(final String nomNouvelleColonne, final Class<?> type) {
        ++this.nbColonnes;
        final TableStockage tableS = new TableStockage(this.nbLignes, this.nbColonnes);
        for (int j = 0; j < this.nbColonnes - 1; ++j) {
            tableS.setColumnName(j, this.ts.getColumnName(j));
        }
        tableS.setColumnName(this.nbColonnes - 1, nomNouvelleColonne);
        for (int i = 0; i < this.nbLignes - 1; ++i) {
            for (int k = 0; k < this.nbColonnes - 1; ++k) {
                tableS.setValueAt(this.ts.getValueAt(i, k), i, k);
            }
            tableS.setColumnClass(this.nbColonnes - 1, type);
        }
        return tableS;
    }
    
    public TableStockage ajouterLigne() {
        if (this.nbColonnes > 0) {
            ++this.nbLignes;
            final TableStockage tableS = new TableStockage(this.nbLignes, this.nbColonnes);
            for (int j = 0; j < this.nbColonnes; ++j) {
                tableS.setColumnName(j, this.ts.getColumnName(j));
            }
            for (int i = 0; i < this.nbLignes - 2; ++i) {
                for (int k = 0; k < this.nbColonnes; ++k) {
                    tableS.setValueAt(this.ts.getValueAt(i, k), i, k);
                }
            }
            for (int j = 0; j < this.nbColonnes; ++j) {
                if (this.ts.getColumnClass(j).equals(String.class)) {
                    tableS.setValueAt("", this.nbLignes - 2, j);
                }
                else if (this.ts.getColumnClass(j).equals(Double.class)) {
                    tableS.setValueAt(new Double("0"), this.nbLignes - 2, j);
                }
                else {
                    tableS.setValueAt(new Boolean(false), this.nbLignes - 2, j);
                }
            }
            return tableS;
        }
        JOptionPane.showMessageDialog(null, "PAS DE COLONNE", "ERREUR", 0);
        return this.ts;
    }
    
    public TableStockage supprimerColonne(final int indice) {
        --this.nbColonnes;
        final TableStockage tableS = new TableStockage(this.nbLignes, this.nbColonnes);
        for (int j = 0; j < indice; ++j) {
            tableS.setColumnName(j, this.ts.getColumnName(j));
        }
        for (int i = 0; i < this.nbLignes - 1; ++i) {
            for (int k = 0; k < indice; ++k) {
                tableS.setValueAt(this.ts.getValueAt(i, k), i, k);
            }
        }
        if (indice != this.nbColonnes) {
            for (int j = indice; j < this.nbColonnes; ++j) {
                tableS.setColumnName(j, this.ts.getColumnName(j + 1));
            }
            for (int i = 0; i < this.nbLignes - 1; ++i) {
                for (int k = indice; k < this.nbColonnes; ++k) {
                    tableS.setValueAt(this.ts.getValueAt(i, k + 1), i, k);
                }
            }
        }
        return tableS;
    }
    
    public TableStockage supprimerLigne(final int indice) {
        --this.nbLignes;
        final TableStockage tableS = new TableStockage(this.nbLignes, this.nbColonnes);
        for (int j = 0; j < this.nbColonnes; ++j) {
            tableS.setColumnName(j, this.ts.getColumnName(j));
        }
        for (int i = 0; i < indice; ++i) {
            for (int k = 0; k < this.nbColonnes; ++k) {
                tableS.setValueAt(this.ts.getValueAt(i, k), i, k);
            }
        }
        if (indice != this.nbLignes - 1) {
            for (int i = indice + 1; i < this.nbLignes; ++i) {
                for (int k = 0; k < this.nbColonnes; ++k) {
                    tableS.setValueAt(this.ts.getValueAt(i, k), i - 1, k);
                }
            }
        }
        return tableS;
    }
    
    public TableStockage separerColonne(final int indice) {
        if (!this.ts.getColumnClass(indice).equals(String.class)) {
            JOptionPane.showMessageDialog(null, "non separable: PAS DE TYPE STRING", "ERREUR", 0);
            return this.ts;
        }
        int k = 0;
        String val = (String)this.ts.getValueAt(0, indice);
        String val2 = new String();
        while (val.charAt(k) != ' ' && k != val.length() - 1) {
            val2 = val2.concat(String.valueOf(val.charAt(k)));
            ++k;
        }
        if (k == val.length() - 1) {
            val2 = new String();
            for (k = 0; val.charAt(k) != ' ' && k != val.length() - 1; ++k) {
                val2 = val2.concat(String.valueOf(val.charAt(k)));
            }
        }
        if (k != val.length() - 1) {
            ++this.nbColonnes;
            final TableStockage tableS = new TableStockage(this.nbLignes, this.nbColonnes);
            val2 = new String();
            k = 0;
            for (int j = 0; j < indice; ++j) {
                tableS.setColumnName(j, this.ts.getColumnName(j));
            }
            for (int j = indice + 1; j < this.nbColonnes; ++j) {
                tableS.setColumnName(j, this.ts.getColumnName(j - 1));
            }
            final String colonne = this.ts.getColumnName(indice);
            final String[] resNP = colonne.split(" ", 2);
            final String val1Colonne = resNP[0];
            String val2Colonne;
            if (resNP.length > 1) {
                val2Colonne = resNP[1];
            }
            else {
                val2Colonne = "";
            }
            tableS.setColumnName(indice, val1Colonne);
            tableS.setColumnName(indice + 1, val2Colonne);
            for (int i = 0; i < this.nbLignes - 1; ++i) {
                for (int l = 0; l < indice; ++l) {
                    tableS.setValueAt(this.ts.getValueAt(i, l), i, l);
                }
                for (int l = indice + 2; l < this.nbColonnes; ++l) {
                    tableS.setValueAt(this.ts.getValueAt(i, l - 1), i, l);
                }
            }
            String val3 = null;
            for (int m = 0; m < this.nbLignes - 1; ++m) {
                val2 = new String();
                val = (String)this.ts.getValueAt(m, indice);
                k = 0;
                if (val.equals("")) {
                    val2 = "";
                    val3 = "";
                }
                else {
                    while (val.charAt(k) != ' ' && k != val.length() - 1) {
                        val2 = val2.concat(String.valueOf(val.charAt(k)));
                        ++k;
                    }
                    if (k == val.length() - 1) {
                        val2 = new String();
                        for (k = 0; val.charAt(k) != ' ' && k != val.length() - 1; ++k) {
                            val2 = val2.concat(String.valueOf(val.charAt(k)));
                        }
                    }
                    val3 = val.substring(k + 1, val.length());
                }
                double int1 = 0.0;
                double int2 = 0.0;
                try {
                    int1 = Double.parseDouble(val2);
                    tableS.setValueAt(int1, m, indice);
                }
                catch (NumberFormatException e) {
                    tableS.setValueAt(val2, m, indice);
                }
                try {
                    int2 = Double.parseDouble(val3);
                    tableS.setValueAt(int2, m, indice + 1);
                }
                catch (NumberFormatException e) {
                    tableS.setValueAt(val3, m, indice + 1);
                }
            }
            return tableS;
        }
        JOptionPane.showMessageDialog(null, "PAS SEPARABLE", "ERREUR", 0);
        return this.ts;
    }
    
    public TableStockage fusionnerColonnes(final int indice) {
        if (this.nbColonnes <= 1) {
            JOptionPane.showMessageDialog(null, "UNE SEULE COLONNE", "ERREUR", 0);
            return this.ts;
        }
        if (!this.ts.getColumnClass(indice).equals(Boolean.class) && !this.ts.getColumnClass(indice + 1).equals(Boolean.class)) {
            --this.nbColonnes;
            final TableStockage tableS = new TableStockage(this.nbLignes, this.nbColonnes);
            for (int j = 0; j < indice; ++j) {
                tableS.setColumnName(j, this.ts.getColumnName(j));
            }
            final String c1 = this.ts.getColumnName(indice);
            final String c2 = this.ts.getColumnName(indice + 1);
            for (int i = indice + 1; i < this.nbColonnes; ++i) {
                tableS.setColumnName(i, this.ts.getColumnName(i + 1));
            }
            tableS.setColumnName(indice, c1.concat(" " + c2));
            for (int k = 0; k < this.nbLignes - 1; ++k) {
                for (int l = 0; l < indice; ++l) {
                    tableS.setValueAt(this.ts.getValueAt(k, l), k, l);
                }
                final Object d1 = this.ts.getValueAt(k, indice);
                final Object d2 = this.ts.getValueAt(k, indice + 1);
                final String s1 = d1.toString();
                final String s2 = d2.toString();
                tableS.setValueAt(s1.concat(" " + s2), k, indice);
                for (int m = indice + 1; m < this.nbColonnes; ++m) {
                    tableS.setValueAt(this.ts.getValueAt(k, m + 1), k, m);
                }
            }
            return tableS;
        }
        JOptionPane.showMessageDialog(null, "PAS DE TYPE STRING", "ERREUR", 0);
        return this.ts;
    }
    
    public TableStockage colonneMail() {
        ++this.nbColonnes;
        final ModifTableStockage mt = new ModifTableStockage(this.ts);
        TableStockage tableS = new TableStockage(this.nbLignes, this.nbColonnes);
        tableS = mt.ajouterColonne("Envoyer E-Mail a:", Boolean.class);
        for (int i = 0; i < this.nbLignes - 1; ++i) {
            tableS.setValueAt(new Boolean(true), i, this.nbColonnes - 1);
        }
        return tableS;
    }
    
    public TableStockage selectMail() {
        int j;
        for (j = 0; j < this.nbColonnes && !this.ts.getColumnName(j).equals("Envoyer E-Mail a:"); ++j) {}
        if (j != this.nbColonnes) {
            int newNbLignes = 0;
            for (int i = 0; i < this.nbLignes - 1; ++i) {
                final Boolean bool = (Boolean)this.ts.getValueAt(i, j);
                if (bool) {
                    ++newNbLignes;
                }
            }
            final TableStockage tableS = new TableStockage(newNbLignes + 1, this.nbColonnes);
            int k = 0;
            for (int ind = 0; ind < this.nbColonnes; ++ind) {
                tableS.setColumnName(ind, this.ts.getColumnName(ind));
            }
            for (int row = 0; row < this.nbLignes - 1; ++row) {
                Boolean bool = (Boolean)this.ts.getValueAt(row, j);
                if (bool) {
                    for (int column = 0; column < this.nbColonnes; ++column) {
                        tableS.setValueAt(this.ts.getValueAt(row, column), k, column);
                        bool = false;
                    }
                    ++k;
                }
            }
            return tableS;
        }
        JOptionPane.showMessageDialog(null, "Le mail sera envoye a toute la liste", "Information", 1);
        return this.ts;
    }
}
