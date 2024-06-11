package geo.google.mapping;
import static org.junit.Assert.*;
import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;
import org.junit.Test;


public class AddressToUsAddressFunctorTest {
    
    @Test
    public void testExecute() {
        GeoAddress addr = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        addr.setAddressDetails(addressDetails);
        
        GeoUsAddress expected = new GeoUsAddress();
        GeoUsAddress actual = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertEquals(expected, actual);
        
        List<AddressDetails.Country.CountryNameCode> countryNames = new ArrayList<>();
        countryNames.add(new AddressDetails.Country.CountryNameCode());
        addressDetails.getCountry().setCountryNameCode(countryNames);
        actual = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertEquals(expected, actual);
        
        AdministrativeArea adminArea = new AdministrativeArea();
        addressDetails.getCountry().setAdministrativeArea(adminArea);
        actual = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertEquals(expected, actual);
        
        List<AdministrativeArea.AdministrativeAreaName> adminAreaNames = new ArrayList<>();
        adminAreaNames.add(new AdministrativeArea.AdministrativeAreaName());
        adminArea.setAdministrativeAreaName(adminAreaNames);
        actual = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertEquals(expected, actual);
        
        AdministrativeArea.SubAdministrativeArea subAdminArea = new AdministrativeArea.SubAdministrativeArea();
        adminArea.setSubAdministrativeArea(subAdminArea);
        actual = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertEquals(expected, actual);
        
        List<AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName> subAdminAreaNames = new ArrayList<>();
        subAdminAreaNames.add(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName());
        subAdminArea.setSubAdministrativeAreaName(subAdminAreaNames);
        actual = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertEquals(expected, actual);
        
        Locality locality = new Locality();
        adminArea.setLocality(locality);
        actual = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertEquals(expected, actual);
        
        List<Locality.LocalityName> localityNames = new ArrayList<>();
        localityNames.add(new Locality.LocalityName());
        locality.setLocalityName(localityNames);
        actual = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertEquals(expected, actual);
        
        PostalCode postalCode = new PostalCode();
        locality.setPostalCode(postalCode);
        actual = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertEquals(expected, actual);
        
        List<PostalCode.PostalCodeNumber> postalCodeNumbers = new ArrayList<>();
        postalCodeNumbers.add(new PostalCode.PostalCodeNumber());
        postalCode.setPostalCodeNumber(postalCodeNumbers);
        actual = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertEquals(expected, actual);
        
        Thoroughfare thoroughfare = new Thoroughfare();
        thoroughfare.getThoroughfareName().add(new ThoroughfareNameType());
        locality.setThoroughfare(thoroughfare);
        actual = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertEquals(expected, actual);
        
        thoroughfare.getThoroughfareName().add(new ThoroughfareNameType());
        actual = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertEquals(expected, actual);
        
        addr.setCoordinate(new GeoCoordinate());
        actual = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertEquals(expected, actual);
        
        addr.setAccuracy(0.0);
        actual = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertEquals(expected, actual);
    }
}