package geo.google.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import geo.google.datamodel.AddressDetails;
import geo.google.datamodel.AdministrativeArea;
import geo.google.datamodel.Locality;
import geo.google.datamodel.PostalCode;
import geo.google.datamodel.ThoroughfareNameType;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.Country;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.Country.CountryNameCode;
import oasis.names.tc.ciq.xsdschema.xal._2.AdministrativeArea.SubAdministrativeArea;
import oasis.names.tc.ciq.xsdschema.xal._2.AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName;
import oasis.names.tc.ciq.xsdschema.xal._2.Locality.LocalityName;
import oasis.names.tc.ciq.xsdschema.xal._2.PostalCode.PostalCodeNumber;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute() {
        // Create test data
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress addr = new GeoAddress();
        AddressDetails addrDetails = new AddressDetails();
        
        Country country = new Country();
        CountryNameCode countryCode1 = new CountryNameCode();
        countryCode1.setContent("US");
        CountryNameCode countryCode2 = new CountryNameCode();
        countryCode2.setContent("USA");
        List<CountryNameCode> countryNames = new ArrayList<>();
        countryNames.add(countryCode1);
        countryNames.add(countryCode2);
        country.setCountryNameCode(countryNames);
        
        AdministrativeArea adminArea = new AdministrativeArea();
        AdministrativeAreaName adminAreaName = new AdministrativeAreaName();
        adminAreaName.setContent("California");
        List<AdministrativeAreaName> names = new ArrayList<>();
        names.add(adminAreaName);
        adminArea.setAdministrativeAreaName(names);
        
        SubAdministrativeArea subAdminArea = new SubAdministrativeArea();
        SubAdministrativeAreaName subAdminAreaName = new SubAdministrativeAreaName();
        subAdminAreaName.setContent("San Francisco");
        List<SubAdministrativeAreaName> subNames = new ArrayList<>();
        subNames.add(subAdminAreaName);
        subAdminArea.setSubAdministrativeAreaName(subNames);
        
        Locality locality = new Locality();
        LocalityName localityName = new LocalityName();
        localityName.setContent("San Francisco");
        List<LocalityName> localNames = new ArrayList<>();
        localNames.add(localityName);
        locality.setLocalityName(localNames);
        
        PostalCode postalCode = new PostalCode();
        PostalCodeNumber postalCodeNumber = new PostalCodeNumber();
        postalCodeNumber.setContent("94101");
        List<PostalCodeNumber> numbers = new ArrayList<>();
        numbers.add(postalCodeNumber);
        postalCode.setPostalCodeNumber(numbers);
        
        ThoroughfareNameType thoroughfare = new ThoroughfareNameType();
        thoroughfare.setContent("123 Main St");
        ThoroughfareNameType thoroughfare2 = new ThoroughfareNameType();
        thoroughfare2.setContent("Apt 4");
        List<ThoroughfareNameType> thoroughfares = new ArrayList<>();
        thoroughfares.add(thoroughfare);
        thoroughfares.add(thoroughfare2);
        
        locality.setThoroughfare(new Thoroughfare());
        locality.getThoroughfare().setThoroughfareName(thoroughfares);
        
        adminArea.setSubAdministrativeArea(subAdminArea);
        country.setAdministrativeArea(adminArea);
        addrDetails.setCountry(country);
        addr.setAddressDetails(addrDetails);

        // Execute the method and assert the result
        GeoUsAddress result = functor.execute(addr);
        assertEquals("US", result.getCountry());
        assertEquals("California", result.getState());
        assertEquals("San Francisco", result.getCounty());
        assertEquals("San Francisco", result.getCity());
        assertEquals("94101", result.getPostalCode());
        assertEquals("123 Main St", result.getAddressLine1());
        assertEquals("Apt 4", result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
    }
}