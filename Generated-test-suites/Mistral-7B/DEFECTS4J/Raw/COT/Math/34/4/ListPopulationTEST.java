import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructorWithPositivePopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        Assert.assertEquals(5, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorWithNegativePopulationLimit() {
        ListPopulation population = new ListPopulation(-1);
    }

    @Test
    public void testListPopulationAddChromosomes() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome() {}; // assuming Chromosome is an abstract class that can be instantiated
        Chromosome chromosome2 = new Chromosome() {};
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        population.addChromosomes(chromosomes);
        List<Chromosome> populationChromosomes = population.getChromosomes();
        Assert.assertEquals(5, population.getPopulationSize());
        Assert.assertEquals(chromosome1, populationChromosomes.get(0));
        Assert.assertEquals(chromosome2, populationChromosomes.get(1));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomesWithSizeGreaterThanPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome() {};
        Chromosome chromosome2 = new Chromosome() {};
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(new Chromosome() {}); // adding an extra chromosome
        population.addChromosomes(chromosomes);
    }

    @Test
    public void testListPopulationSetChromosomes() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome() {};
        Chromosome chromosome2 = new Chromosome() {};
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        population.setChromosomes(chromosomes);
        List<Chromosome> populationChromosomes = population.getChromosomes();
        Assert.assertEquals(5, population.getPopulationSize());
        Assert.assertEquals(chromosome1, populationChromosomes.get(0));
        Assert.assertEquals(chromosome2, populationChromosomes.get(1));
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationSetChromosomesWithNullChromosomes() {
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationSetChromosomesWithSizeGreaterThanPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome() {};
        Chromosome chromosome2 = new Chromosome() {};
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(new Chromosome() {}); // adding an extra chromosome
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        ListPopulation population = new ListPopulation(3);
        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double fitness() {
                return 1.5;
            }
        };
        Chromosome chromosome2 = new Chromosome() {
            @Override
            public double fitness() {
                return 1.0;
            }
        };
        Chromosome chromosome3 = new Chromosome() {
            @Override
            public double fitness() {
                return 2.0;
            }
        };
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        population.addChromosome(chromosome3);
        Chromosome fittestChromosome = population.getFittestChromosome();
        Assert.assertEquals(chromosome3, fittestChromosome);
    }

    @Test
    public void testListPopulationToString() {
        ListPopulation population = new ListPopulation(3);
        Chromosome chromosome1 = new Chromosome() {};
        Chromosome chromosome2 = new Chromosome() {};
        Chromosome chromosome3 = new Chromosome() {};
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        population.addChromosome(chromosome3);
        Assert.assertEquals("[Chromosome, Chromosome, Chromosome]", population.toString());
    }
}